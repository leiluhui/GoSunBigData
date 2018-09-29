package com.hzgc.cluster.spark.consumer

import java.sql.DriverManager
import java.util.Date

import com.hzgc.common.service.imsi.ImsiInfo
import com.hzgc.common.util.json.JacksonUtil
import kafka.serializer.StringDecoder
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaToTidb {
  def main(args: Array[String]): Unit = {

    val dbc = "jdbc:mysql://172.18.18.100:3306/zhaozhe?user=root&password=Hzgc@123"
    classOf[com.mysql.jdbc.Driver]
    val driver = "com.mysql.jdbc.Driver"
    Class.forName(driver)

    val log: Logger = Logger.getLogger(KafkaToParquet.getClass)
    val appName = "KafkaToTidb"
    val conf = new SparkConf().setAppName(appName)
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("/tmp/spark_out")
    val kafkaParam = Map[String, String](
      "zookeeper.connect" -> "172.18.18.100:2181",
      "metadata.broker.list" -> "172.18.18.100:9092",
      "group.id" -> "1",
      "zookeeper.connection.timeout.ms" -> "10000"
    )
    val topicSet = Set("imsi")
    val directKafka: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParam, topicSet)
    val windowed: DStream[(String, Int)] = directKafka.map(tuple2 => {
      val imsiObject = JacksonUtil.toObject(tuple2._2, classOf[ImsiInfo])
      val imsi = imsiObject.getImsi
      (imsi,1)
    })
    //second1:窗口长度，second2:滑动间隔
    val result: DStream[(String, Int)] = windowed.reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(3600), Seconds(60))
    result.filter(x=> x._2 >= 10).foreachRDD(it => {
        it.foreachPartition(datas => {
          val conn = DriverManager.getConnection(dbc)
          val prep = conn.prepareStatement("INSERT INTO t_imsi_reduce (imsi,number,savetime) VALUES (?, ?, ?) ")
          datas.foreach(data => {
            val imsi: String = data._1
            val num = data._2
            val time = new Date().getTime
            prep.setString(1, imsi)
            prep.setInt(2, num)
            prep.setLong(3, time)
            prep.executeUpdate
            println("===========imsi="+imsi+", num="+num+", time="+time)
          })
        })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
