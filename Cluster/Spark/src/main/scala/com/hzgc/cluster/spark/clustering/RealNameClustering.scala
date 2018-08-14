package com.hzgc.cluster.spark.clustering

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.hzgc.cluster.spark.util.{FaceObjectUtil, PropertiesUtil}
import com.hzgc.common.collect.bean.FaceObject
import com.hzgc.common.facedispatch.DeviceUtilImpl
import com.hzgc.common.facestarepo.table.alarm.StaticRepoUtil
import com.hzgc.common.hbase.HBaseHelper
import com.hzgc.common.util.json.JSONUtil
import com.hzgc.jni.FaceFunction
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.{JavaConverters, immutable, mutable}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object RealNameClustering extends Serializable {

  case class Json(staticID: String, staticObjectType: String, sim: Float)

  val LOG: Logger = Logger.getLogger(RealNameClustering.getClass)

  def main(args: Array[String]): Unit = {
    val deviceUtil = new DeviceUtilImpl
    val properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.clustering.appName")
    val itemNum = properties.getProperty("job.recognizeAlarm.items.num").toInt
    val timeInterval = Durations.seconds(properties.getProperty("job.Alarm.timeInterval").toLong)
    val jdbcUrl = properties.getProperty("phoenix.jdbc.url")
    val conf = new SparkConf().setAppName(appName)
    val ssc = new StreamingContext(conf, timeInterval)
    val kafkaBootStrapBroadCast = ssc.sparkContext.broadcast(properties.getProperty("kafka.metadata.broker.list"))
    val jdbcUrlBroadCast = ssc.sparkContext.broadcast(properties.getProperty("phoenix.jdbc.url"))
    val kafkaGroupId = properties.getProperty("kafka.realName.group.id")
    val topics = Set(properties.getProperty("kafka.topic.name"))
    val brokers = properties.getProperty("kafka.metadata.broker.list")
    val kafkaParams = Map(
      "metadata.broker.list" -> brokers,
      "group.id" -> kafkaGroupId
    )
    LOG.info("appName" + appName)
    val kafkaDynamicPhoto = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    LOG.info("topics :" + topics)
    LOG.info("kafkaboot is : ============" + kafkaBootStrapBroadCast.value)
    LOG.info("jdbcUrl is : ==================" + jdbcUrlBroadCast.value)
    val jsonResult = kafkaDynamicPhoto.map(data => (data._1, FaceObjectUtil.jsonToObject(data._2)))
      .filter(obj => obj._2.getAttribute.getFeature != null && obj._2.getAttribute.getFeature.length == 512)
      .map(message => {
        LOG.info("kafkaboot is : +++++++++++++++" + kafkaBootStrapBroadCast.value)
        LOG.info("jdbcUrl is : +++++++++++++++++" + jdbcUrlBroadCast.value)
        val totalList = JavaConverters
          .asScalaBufferConverter(StaticRepoUtil.getInstance(kafkaBootStrapBroadCast.value, jdbcUrlBroadCast.value)
            .getTotalList).asScala
        val faceObj = message._2
        LOG.info("The big url of the faceObject is " + faceObj.getBurl)
        val ipcID = faceObj.getIpcId
        val filterResult = new ArrayBuffer[Json]()
        val table = HBaseHelper.getTable("peoplescheduler")
        val get = new Get(Bytes.toBytes("RealNameRule"))
        val rs = table.get(get)
        val rules = Bytes.toString(rs.getValue(Bytes.toBytes("rules"), Bytes.toBytes("rule")))
        LOG.info("rules is : +++++++++" + rules)
        val obj = JSONUtil.toObject(rules, classOf[Rules])
        val realnamelist = obj.getRealNames
        while (realnamelist.iterator().hasNext) {
          val realName = realnamelist.iterator().next()
          if (realName.getIpcIds.contains(ipcID)) {
            val sim = realName.getSim.toInt
            totalList.foreach(record => {
              val threshold = FaceFunction.featureCompare(record(2).asInstanceOf[Array[Float]], faceObj.getAttribute.getFeature)
              if (threshold > sim) {
                filterResult += Json(record(0).asInstanceOf[String], record(1).asInstanceOf[String], threshold)
              }
            })
          }
        }
        (message._1, message._2, ipcID, filterResult)
      })

    val putToHBase = jsonResult.foreachRDD(forRDD => {
      forRDD.foreachPartition(parRDD => {
        val hbaseTable: Table = HBaseHelper.getTable("peopleadd")
        val hbaseTableReco: Table = HBaseHelper.getTable("peoplerecognize")
        val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val list = new util.ArrayList[String]()
        LOG.info("time  is ++++++++++++++++++++")
        parRDD.foreach(obj => {
          val finalList = obj._4
          if (finalList.isEmpty) {
            val faceobj = obj._2
            val surl = faceobj.getSurl
            val burl = faceobj.getBurl
            val alarm_time = df.format(new Date())
            val ipcid = obj._3
            val rowkey = ipcid + "_" + alarm_time
            val hostname = faceobj.getHostname
            val feature = faceobj.getAttribute.getFeature
            val put: Put = new Put(Bytes.toBytes(rowkey))
            LOG.info("rowkey is : ++++++++++++++++" + rowkey)
            put.addColumn(Bytes.toBytes("add"), Bytes.toBytes("faceobject"), Bytes.toBytes(JSONUtil.toJson(faceobj)))
            hbaseTable.put(put)
          } else {
            val updateTimeList = new java.util.ArrayList[String]()
            finalList.foreach(message => {
              val scan: Scan = new Scan()
              val rs = hbaseTableReco.getScanner(scan)
              while (rs.iterator().hasNext) {
                val result = rs.iterator().next()
                val rowkey = Bytes.toString(result.getRow)
                list.add(rowkey)
              }
              if (list.contains(message.staticID)) {
                val get = new Get(Bytes.toBytes(message.staticID))
                val r = hbaseTableReco.get(get)
                val listString = Bytes.toString(r.getValue(Bytes.toBytes("recognize"), Bytes.toBytes("faceobject")))
                var list  = JSONUtil.toObject(listString, util.Arrays.asList[FaceObject]().getClass)
//                list.add(obj._2)
                list.add(obj._2)
                val put = new Put(Bytes.toBytes(message.staticID))
                put.addColumn(Bytes.toBytes("recognize"), Bytes.toBytes("faceobj"), Bytes.toBytes(JSONUtil.toJson(list)))
                hbaseTableReco.put(put)
              } else {
                val put = new Put(Bytes.toBytes(message.staticID))
                val peopleList = new util.ArrayList[FaceObject]()
                peopleList.add(obj._2)
                put.addColumn(Bytes.toBytes("recognize"), Bytes.toBytes("faceobj"), Bytes.toBytes(JSONUtil.toJson(peopleList)))
                hbaseTableReco.put(put)
              }
              updateTimeList.add(message.staticID)
            })
            StaticRepoUtil
              .getInstance(kafkaBootStrapBroadCast.value, jdbcUrlBroadCast.value).updateObjectInfoTime(updateTimeList)
          }
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}


























