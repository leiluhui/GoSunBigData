package com.hzgc.cluster.spark.alarm

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.Gson
import com.hzgc.cluster.spark.message.OffLineAlarmMessage
import com.hzgc.cluster.spark.util.PropertiesUtil
import com.hzgc.common.facedispatch.DeviceUtilImpl
import com.hzgc.common.facedispatch.table.DispatchTable
import com.hzgc.common.facestarepo.table.alarm.StaticRepoUtil
import com.hzgc.common.rocketmq.RocketMQProducer
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters

/**
  * 人脸识别离线告警任务（刘善彬）
  *
  */
object FaceOffLineAlarmJob {
  
  def main(args: Array[String]): Unit = {
    val offLineAlarmMessage = new OffLineAlarmMessage()
    val properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.offLine.appName")
    val mqTopic = properties.getProperty("rocketmq.topic.name")
    val nameServer = properties.getProperty("rocketmq.nameserver")
    val grouId = properties.getProperty("rocketmq.group.id")
    val conf = new SparkConf().setAppName(appName)
    val sc = new SparkContext(conf)
    val kafkaBootStrapBroadCast = sc.broadcast(properties.getProperty("kafka.metadata.broker.list"))
    val jdbcUrlBroadCast = sc.broadcast(properties.getProperty("phoenix.jdbc.url"))
    val deviceUtilImpl = new DeviceUtilImpl()
    val offLineAlarmRule = deviceUtilImpl.getThreshold
    val separator = "ZHONGXIAN"
    if (offLineAlarmRule != null && !offLineAlarmRule.isEmpty) {
      println("Start offline alarm task data processing ...")
      val objTypeList = PropertiesUtil.getOffLineArarmObjType(offLineAlarmRule)
      val returnResult = StaticRepoUtil
        .getInstance(kafkaBootStrapBroadCast.value, jdbcUrlBroadCast.value)
        .searchByPkeysUpdateTime(objTypeList)
      if (returnResult != null && !returnResult.isEmpty) {
        val totalData = sc.parallelize(JavaConverters.asScalaBufferConverter(returnResult).asScala)
        val splitResult = totalData.map(totailDataElem => (totailDataElem.split(separator)(0), totailDataElem.split(separator)(1), totailDataElem.split(separator)(2)))
        val getDays = splitResult.map(splitResultElem => {
          val objRole = offLineAlarmRule.get(splitResultElem._2)
          if (objRole == null && objRole.isEmpty) {
            (splitResultElem._1, splitResultElem._2, splitResultElem._3, null)
          } else {
            val days = PropertiesUtil.getSimilarity(objRole)
            (splitResultElem._1, splitResultElem._2, splitResultElem._3, days)
          }
        }).filter(_._4 != null)
        val filterResult = getDays.filter(getFilter => getFilter._3 != null && getFilter._3.length != 0).
          map(getDaysElem => (getDaysElem._1, getDaysElem._2, getDaysElem._3, PropertiesUtil.timeTransition(getDaysElem._3), getDaysElem._4)).
          filter(ff => ff._4 != null && ff._4.length != 0).
          filter(filter => filter._4 > filter._5.toString)
        //将离线告警信息推送到MQ()
        filterResult.foreach(filterResultElem => {
          val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          val dateStr = df.format(new Date())
          val rocketMQProducer = RocketMQProducer.getInstance(nameServer, mqTopic, grouId)
          val offLineAlarmMessage = new OffLineAlarmMessage()
          val gson = new Gson()
          offLineAlarmMessage.setAlarmType(DispatchTable.OFFLINE)
          offLineAlarmMessage.setStaticID(filterResultElem._1)
          offLineAlarmMessage.setUpdateTime(filterResultElem._3)
          offLineAlarmMessage.setAlarmTime(dateStr)
          val alarmStr = gson.toJson(offLineAlarmMessage)
          //离线告警信息推送的时候，平台id为对象类型字符串的前4个字节。
          //          val platID = filterResultElem._2.substring(0, 4)
          //由于平台那边取消了平台ID的概念,以后默认为0001
          rocketMQProducer.
            send("0001", "alarm_" + DispatchTable.OFFLINE, filterResultElem._1 + dateStr, alarmStr.getBytes(), null)
        })
      } else {
        println("No data was received from the static repository,the task is not running！")
      }
    } else {
      println("No object type alarm dispatched offline,the task is not running")
    }
    sc.stop()
  }


}
