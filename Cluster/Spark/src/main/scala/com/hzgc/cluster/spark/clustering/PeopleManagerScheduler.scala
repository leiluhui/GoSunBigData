package com.hzgc.cluster.spark.clustering

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.hzgc.cluster.spark.util.{FaceObjectUtil, PropertiesUtil}
import com.hzgc.common.collect.bean.FaceObject
import com.hzgc.common.collect.facedis.FtpRegisterClient
import com.hzgc.common.faceclustering.table.{ClusteringTable, PeopleRecognizeTable, PeopleSchedulerTable, PersonRegionTable}
import com.hzgc.common.facedispatch.DeviceUtilImpl
import com.hzgc.common.facestarepo.table.alarm.{ResidentUtil, StaticRepoUtil}
import com.hzgc.common.hbase.HBaseHelper
import com.hzgc.common.util.empty.IsEmpty
import com.hzgc.common.util.json.JSONUtil
import com.hzgc.jni.{FaceAttribute, FaceFunction}
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.JavaConverters
import scala.collection.mutable.ArrayBuffer

object PeopleManagerScheduler extends Serializable {

  case class Json(staticID: String, staticObjectType: String, sim: Float)

  val LOG: Logger = Logger.getLogger(PeopleManagerScheduler.getClass)

  def main(args: Array[String]): Unit = {
    val deviceUtil = new DeviceUtilImpl
    val properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.people.manager.appName")
    val timeInterval = Durations.seconds(properties.getProperty("job.people.manager.timeInterval").toLong)
    val jdbcUrl = properties.getProperty("phoenix.jdbc.url")
    val conf = new SparkConf().setAppName(appName)
    val ssc = new StreamingContext(conf, timeInterval)
    val kafkaBootStrapBroadCast = ssc.sparkContext.broadcast(properties.getProperty("kafka.metadata.broker.list"))
    val jdbcUrlBroadCast = ssc.sparkContext.broadcast(properties.getProperty("phoenix.jdbc.url"))
    val kafkaGroupId = properties.getProperty("kafka.people.manager.group.id")
    val topics = Set(properties.getProperty("kafka.topic.name"))
    val brokers = properties.getProperty("kafka.metadata.broker.list")
    val kafkaParams = Map(
      "metadata.broker.list" -> brokers,
      "group.id" -> kafkaGroupId
    )
    LOG.info("appName" + appName)
    val kafkaDynamicPhoto = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    LOG.info("topics :" + topics)
    val jsonResult = kafkaDynamicPhoto.map(data => (data._1, FaceObjectUtil.jsonToObject(data._2)))
      .filter(obj => obj._2.getAttribute.getFeature != null && obj._2.getAttribute.getFeature.length == 512)
      .map(message => {
        val totalList = JavaConverters
                  .asScalaBufferConverter(ResidentUtil.getInstance(jdbcUrlBroadCast.value).getTotalList).asScala
        LOG.info("The ResidentUtil's Size is : " + totalList.size)
        val faceObj = message._2
        LOG.info("The big url of the faceObject is " + faceObj.getBurl)
        val ipcID = faceObj.getIpcId
        val filterResult = new ArrayBuffer[Json]()
        val table = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME)
        val regionTable = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME)
        val scan = new Scan()
        val resultScanner = table.getScanner(scan)
        val iterator = resultScanner.iterator()
        while (iterator.hasNext) {
          val result = iterator.next()
          val regionId = result.getRow
          LOG.info("regionId is : " + Bytes.toString(regionId))
          val get = new Get(regionId)
          val regionResult = regionTable.get(get)
          val ipcidStr = Bytes.toString(regionResult.getValue(PersonRegionTable.COLUMNFAMILY, PersonRegionTable.REGION_IPCIDS))
          val ipcidList = JSONUtil.toObject(ipcidStr, new util.ArrayList[String]().getClass)
          if (ipcidList.contains(ipcID)) {
            val sim = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.SIM)).toInt
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
        val hbaseTableAdd: Table = HBaseHelper.getTable(ClusteringTable.TABLE_PEOPLECOMPARE)
        val hbaseTableReco: Table = HBaseHelper.getTable(PeopleRecognizeTable.TABLE_NAME)
        val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val list = new util.ArrayList[String]()
        parRDD.foreach(obj => {
          val finalList = obj._4
          if (finalList.isEmpty) {
            val faceobj = obj._2
            val alarm_time = df.format(new Date())
            val ipcid = obj._3
            val rowkey = ipcid + "_" + alarm_time.replaceAll("[^\\d]+","")
            val hostname = faceobj.getHostname
            val feature = faceobj.getAttribute.getFeature
            val put: Put = new Put(Bytes.toBytes(rowkey))
            LOG.info("rowkey is : " + rowkey)
            put.addColumn(ClusteringTable.PEOPELCOMPARE_COLUMNFAMILY, ClusteringTable.PEOPELCOMPARE_COLUMNDATA, Bytes.toBytes(JSONUtil.toJson(faceobj)))
            hbaseTableAdd.put(put)
          } else {
            val updateTimeList = new java.util.ArrayList[String]()
            finalList.foreach(message => {
              val scan: Scan = new Scan()
              val rs = hbaseTableReco.getScanner(scan)
              val iterator = rs.iterator()
              while (iterator.hasNext) {
                val result = iterator.next()
                val rowkey = Bytes.toString(result.getRow)
                list.add(rowkey)
              }
              if (list.contains(message.staticID)) {
                val get = new Get(Bytes.toBytes(message.staticID))
                val r = hbaseTableReco.get(get)
                val listString = Bytes.toString(r.getValue(PeopleRecognizeTable.COLUMNFAMILY, PeopleRecognizeTable.FACEOBJECT))
                val list1: util.ArrayList[FaceObject] = JSONUtil.toObject(listString, new util.ArrayList[FaceObject]().getClass)
                val faceObject = obj._2
                faceObject.setAttribute(null)
                list1.add(faceObject)
                val put = new Put(Bytes.toBytes(message.staticID))
                put.addColumn(PeopleRecognizeTable.COLUMNFAMILY, PeopleRecognizeTable.FACEOBJECT, Bytes.toBytes(JSONUtil.toJson(list1)))
                hbaseTableReco.put(put)
              } else {
                val put = new Put(Bytes.toBytes(message.staticID))
                val peopleList = new util.ArrayList[FaceObject]()
                val faceObject = obj._2
                faceObject.setAttribute(null)
                peopleList.add(faceObject)
                put.addColumn(PeopleRecognizeTable.COLUMNFAMILY, PeopleRecognizeTable.FACEOBJECT, Bytes.toBytes(JSONUtil.toJson(peopleList)))
                hbaseTableReco.put(put)
              }
              updateTimeList.add(message.staticID)
            })
            ResidentUtil.getInstance(jdbcUrlBroadCast.value).updatePeopleManagerTime(updateTimeList)
          }
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}


























