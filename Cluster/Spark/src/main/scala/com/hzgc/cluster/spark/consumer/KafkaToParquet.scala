package com.hzgc.cluster.spark.consumer

import java.sql.Timestamp
import java.util.Properties
import com.google.common.base.Stopwatch
import com.hzgc.cluster.spark.util.PropertiesUtil
import com.hzgc.common.collect.bean.{CarObject, FaceObject, PersonObject}
import com.hzgc.common.util.json.JSONUtil
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.ZkUtils
import org.I0Itec.zkclient.ZkClient
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.elasticsearch.spark.rdd.EsSpark


object KafkaToParquet {
  val log: Logger = Logger.getLogger(KafkaToParquet.getClass)
  case class Picture(ftpurl: String, //图片搜索地址
                     //feature：图片特征值 ipcid：设备id timeslot：时间段
                     feature_float: Array[Float],feature_byte: Array[Byte], ipcid: String, timeslot: Int,
                     //timestamp:时间戳 pictype：图片类型 date：时间
                     exacttime: Timestamp, date: String,
                     //人脸属性：眼镜、性别、口罩
                     eyeglasses: Int, gender: Int, mask: Int,
                     //人脸属性：胡子、清晰度评价
                     huzi: Int, age: Int, sharpness: Int)

  def main(args: Array[String]): Unit = {

    val properties: Properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.faceObjectConsumer.appName")
    val brokers = properties.getProperty("job.faceObjectConsumer.broker.list")
    val kafkaGroupId = properties.getProperty("job.faceObjectConsumer.group.id")
    val timeInterval = properties.getProperty("job.faceObjectConsumer.timeInterval")
    val storeAddress = properties.getProperty("job.storeAddress")
    val zkHosts = properties.getProperty("job.zkDirAndPort")
    val faceTopic = Set(properties.getProperty("job.faceObjectConsumer.faceTopic"))
    val personTopic = Set(properties.getProperty("job.faceObjectConsumer.personTopic"))
    val carTopic = Set(properties.getProperty("job.faceObjectConsumer.carTopic"))
    val zkFacePath = properties.getProperty("job.kafkaToParquet.zkFacePath")
    val zkPersonPath = properties.getProperty("job.kafkaToParquet.zkPersonPath")
    val zkCarPath = properties.getProperty("job.kafkaToParquet.zkCarPath")
    val esNodes = properties.getProperty("job.offLine.esNodes")
    val esPort = properties.getProperty("job.offLine.esPort")

    val zkClient = new ZkClient(zkHosts)
    val conf = new SparkConf().setAppName(appName)
      .set("es.index.auto.create","true")
      .set("es.nodes",esNodes)
      .set("es.port",esPort)
    val spark: SparkSession = SparkSession.builder().config(conf).appName(appName).getOrCreate()
    val sc = spark.sparkContext
    val ssc: StreamingContext = new StreamingContext(sc, Durations.seconds(timeInterval.toLong))
    val kafkaParams: Map[String, String] = Map(
      "metadata.broker.list" -> brokers,
      "group.id" -> kafkaGroupId)

    face2es(spark, ssc, zkClient, kafkaParams, faceTopic, zkHosts, zkFacePath, storeAddress)
    person2es(ssc, zkClient, kafkaParams, personTopic, zkHosts, zkPersonPath)
    car2es(ssc, zkClient, kafkaParams, carTopic, zkHosts, zkCarPath)

    ssc.start()
    ssc.awaitTermination()
  }



  def face2es(spark:SparkSession, ssc:StreamingContext, zkClient: ZkClient, kafkaParams:Map[String, String], topics:Set[String], zkHosts:String, zkPath:String, storeAddress:String): Unit = {
    val messages = createCustomDirectKafkaStream(ssc, kafkaParams, zkHosts, zkPath, topics)
    val kafkaDF  = messages.map(data => (data._1, JSONUtil.toObject(data._2, classOf[FaceObject]))).map(faceobject => {
      (Picture(faceobject._1, faceobject._2.getAttribute.getFeature,faceobject._2.getAttribute.getBitFeature, faceobject._2.getIpcId,
        faceobject._2.getTimeSlot.toInt, Timestamp.valueOf(faceobject._2.getTimeStamp),
        faceobject._2.getDate, faceobject._2.getAttribute.getEyeglasses, faceobject._2.getAttribute.getGender,
        faceobject._2.getAttribute.getMask, faceobject._2.getAttribute.getAge,
        faceobject._2.getAttribute.getHuzi, faceobject._2.getAttribute.getSharpness), faceobject._1, faceobject._2)
    }).foreachRDD(rdd => {
      import spark.implicits._
      rdd.map(rdd => rdd._1).repartition(1).toDF().write.mode(SaveMode.Append).parquet(storeAddress)
      val rddData = rdd.map(data => faceObject2Map(data._2, data._3))
      EsSpark.saveToEs(rddData, "dynamic/person", Map("es.mapping.id"->"ftpurl"))
    })
    messages.foreachRDD(rdd => saveOffsets(zkClient, zkHosts, zkPath, rdd))

  }

  def person2es(ssc:StreamingContext, zkClient: ZkClient, kafkaParams:Map[String, String], topics:Set[String], zkHosts:String, zkPath:String) {
    val messages = createCustomDirectKafkaStream(ssc, kafkaParams, zkHosts, zkPath, topics)
    messages.foreachRDD(rdd => {
      val rddData = rdd.map(data => personObject2Map(data._1, JSONUtil.toObject(data._2, classOf[PersonObject])))
      EsSpark.saveToEs(rddData, "person/recognize", Map("es.mapping.id"->"ftpurl"))
    })
    messages.foreachRDD(rdd => saveOffsets(zkClient, zkHosts, zkPath, rdd))

  }

  def car2es(ssc:StreamingContext, zkClient: ZkClient, kafkaParams:Map[String, String], topics:Set[String], zkHosts:String, zkPath:String) {
    val messages = createCustomDirectKafkaStream(ssc, kafkaParams, zkHosts, zkPath, topics)
    messages.foreachRDD(rdd => {
      val rddData = rdd.map(data => carObject2Map(data._1, JSONUtil.toObject(data._2, classOf[CarObject])))
      EsSpark.saveToEs(rddData, "car/recognize", Map("es.mapping.id"->"ftpurl"))
    })
    messages.foreachRDD(rdd => saveOffsets(zkClient, zkHosts, zkPath, rdd))

  }

  private def createCustomDirectKafkaStream(ssc: StreamingContext, kafkaParams: Map[String, String], zkHosts: String
                                            , zkPath: String, topics: Set[String]): InputDStream[(String, String)] = {
    val topic = topics.last
    val zKClient = new ZkClient(zkHosts)
    val storedOffsets = readOffsets(zKClient, zkHosts, zkPath, topic)
    val kafkaStream = storedOffsets match {
      case None =>
        KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
      case Some(fromOffsets) =>
        val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.key(), mmd.message())
        KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder
          , (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    }
    kafkaStream
  }

  private def readOffsets(zkClient: ZkClient, zkHosts: String, zkPath: String, topic: String): Option[Map[TopicAndPartition, Long]] = {
    log.info("=========================== Read Offsets =============================")
    log.info("Reading offsets from Zookeeper")
    val stopwatch =Stopwatch.createStarted()
    val (offsetsRangesStrOpt, _) = ZkUtils.readDataMaybeNull(zkClient, zkPath)
    offsetsRangesStrOpt match {
      case Some(offsetsRangesStr) =>
        log.info(s"Read offset ranges: $offsetsRangesStr")
        val offsets = offsetsRangesStr.split(",")
          .map(x => x.split(":"))
          .map {
            case Array(partitionStr, offsetStr) => TopicAndPartition(topic, partitionStr.toInt) -> offsetStr.toLong
          }.toMap
        log.info("Done reading offsets from Zookeeper. Took " + stopwatch)
        Some(offsets)
      case None =>
        log.info("No offsets found in Zookeeper. Took " + stopwatch)
        log.info("==================================================================")
        None
    }
  }

  private def saveOffsets(zkClient: ZkClient, zkHosts: String, zkPath: String, rdd: RDD[_]): Unit = {
    log.info("==========================Save Offsets============================")
    log.info("Saving offsets to Zookeeper")
    val stopwatch = Stopwatch.createStarted()
    val offsetsRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
    offsetsRanges.foreach(offsetRange => log.debug(s"Using $offsetRange"))
    val offsetsRangesStr = offsetsRanges.map(offsetRange => s"${offsetRange.partition}:${offsetRange.fromOffset}")
      .mkString(",")
    log.info("Writing offsets to Zookeeper zkClient=" + zkClient + " zkHosts=" + zkHosts + "zkPath=" + zkPath + " offsetsRangesStr:" + offsetsRangesStr)
    ZkUtils.updatePersistentPath(zkClient, zkPath, offsetsRangesStr)
    log.info("Done updating offsets in Zookeeper. Took " + stopwatch)
    log.info("==================================================================")
  }

  def faceObject2Map(ftpUrl:String, faceObject: FaceObject): Map[String, Any] = {
    val face = faceObject.getAttribute
    val map = Map("ftpurl"->ftpUrl,
      "ipcid"->faceObject.getIpcId,
      "timeStamp"->faceObject.getTimeStamp,
      "date"->faceObject.getDate,
      "timeslot"->faceObject.getTimeSlot,
      "surl"->faceObject.getSurl,
      "burl"->faceObject.getBurl,
      "relativePath"->faceObject.getRelativePath,
      "relativePath_big"->faceObject.getRelativePath_big,
      "ip"->faceObject.getIp,
      "hostname"->faceObject.getHostname,

      "age"->face.getAge ,
      "mask"->face.getMask ,
      "gender"->face.getGender ,
      "huzi"->face.getHuzi ,
      "eyeglasses"->face.getEyeglasses,
      "sharpness"->face.getSharpness
    )
    map
  }

  def personObject2Map(ftpUrl:String, personObject: PersonObject): Map[String, String] = {
    val person = personObject.getAttribute
    val map = Map("ftpurl"->ftpUrl,
      "ipcid"->personObject.getIpcId,
      "timestamp"->personObject.getTimeStamp,
      "date"->personObject.getDate,
      "timeslot"->personObject.getTimeSlot.toString,
      "surl"->personObject.getSurl,
      "burl"->personObject.getBurl,
      "relativepath"->personObject.getRelativePath,
      "relativepath_big"->personObject.getRelativePath_big,
      "ip"->personObject.getIp,
      "hostname"->personObject.getHostname,

      "age_code"->person.getAge_code,
      "baby_code"->person.getBaby_code,
      "bag_code"->person.getBag_code,
      "bottomcolor_code"->person.getBottomcolor_code,
      "bottomtype_code"->person.getBottomtype_code,
      "hat_code"->person.getHat_code,
      "hair_code"->person.getHair_code,
      "knapsack_code"->person.getKnapsack_code,
      "messengerbag_code"->person.getMessengerbag_code,
      "orientation_code"->person.getOrientation_code,
      "sex_code"->person.getSex_code,
      "shoulderbag_code"->person.getShoulderbag_code,
      "umbrella_code"->person.getUmbrella_code,
      "uppercolor_code"->person.getUppercolor_code,
      "uppertype_code"->person.getUppertype_code,
      "car_type"->person.getCar_type
    )
    map
  }

  def carObject2Map(ftpUrl:String, carObject:CarObject): Map[String, String] = {
    val car = carObject.getAttribute
    val map = Map("ftpurl"->ftpUrl,
      "ipcid"->carObject.getIpcId,
      "timestamp"->carObject.getTimeStamp,
      "date"->carObject.getDate,
      "timeslot"->carObject.getTimeSlot.toString,
      "surl"->carObject.getSurl,
      "burl"->carObject.getBurl,
      "relativepath"->carObject.getRelativePath,
      "relativepath_big"->carObject.getRelativePath_big,
      "ip"->carObject.getIp,
      "hostname"->carObject.getHostname,

      "vehicle_object_type"->car.getVehicle_object_type ,
      "belt_maindriver"->car.getBelt_maindriver ,
      "belt_codriver"->car.getBelt_codriver ,
      "brand_name"->car.getBrand_name ,
      "call_code"->car.getCall_code ,
      "vehicle_color"->car.getVehicle_color ,
      "crash_code"->car.getCrash_code ,
      "danger_code"->car.getDanger_code ,
      "marker_code"->car.getMarker_code ,
      "plate_schelter_code"->car.getPlate_schelter_code ,
      "plate_flag_code"->car.getPlate_flag_code ,
      "plate_licence"->car.getPlate_licence ,
      "plate_destain_code"->car.getPlate_destain_code ,
      "plate_color_code"->car.getPlate_color_code ,
      "plate_type_code"->car.getPlate_type_code ,
      "rack_code"->car.getRack_code ,
      "sparetire_code"->car.getSparetire_code ,
      "mistake_code"->car.getMistake_code ,
      "sunroof_code"->car.getSunroof_code ,
      "vehicle_type"->car.getVehicle_type
    )
    map
  }

}

