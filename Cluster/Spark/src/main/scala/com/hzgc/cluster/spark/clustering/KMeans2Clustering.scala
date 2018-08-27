package com.hzgc.cluster.spark.clustering

import java.util
import java.util.concurrent.ConcurrentHashMap
import com.hzgc.cluster.spark.util.HBaseTools._
import com.hzgc.cluster.spark.util.{FaceObjectUtil, PropertiesUtil}
import com.hzgc.common.faceclustering.PeopleInAttribute
import com.hzgc.common.faceclustering.table.ClusteringTable
import com.hzgc.common.util.json.JSONUtil
import org.apache.commons.lang.StringUtils
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import scala.collection.mutable.ListBuffer


object KMeans2Clustering {

  val log: Logger = Logger.getLogger(KMeans2Clustering.getClass)

  /**
    * 从HBase获取一系列人脸特征值进行KMeans训练聚合，最后将结果存放入HBase中供前端页面迁入人口查询
    * @param region  聚类区域
    * @param ipcIds  区域系列ID
    * @param appearCount 限制人脸抓拍次数
    * @param startDate 开始日期（yyyyMM）
    * @param endDate  结束日期（yyyyMM）
    */
  def kmeansClustering(region:String, ipcIds:java.util.List[String], appearCount:Int, startDate:String, endDate:String, sc:SparkContext): Boolean = {

    log.info("==========kmeansClustering param : region="+region+",ipcIds="+ipcIds+",appearCount="+appearCount+",startDate="+startDate+",endDate="+endDate)
    if(StringUtils.isBlank(region) || StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)
      || ipcIds == null || ipcIds.isEmpty) {
      log.error("==========KMeans2Clustering parameter[region,ipcIds,appearCount,startDate,endDate] error, kmeansClustering Exit!")
      return false
    }

    val properties = PropertiesUtil.getProperties
    val clusterNum = properties.getProperty("job.clustering.cluster.number")
    val iteratorNum = properties.getProperty("job.clustering.iterator.number")
    val similarityThreshold = properties.getProperty("job.clustering.similarity.Threshold").toDouble
    val center_similarityThreshold: Double = properties.getProperty("job.clustering.similarity.center.Threshold").toDouble
    log.info("==========clusterNum="+clusterNum+",iteratorNum="+iteratorNum+",similarityThreshold="+similarityThreshold+",center_similarityThreshold="+center_similarityThreshold)

    //union data
    var rddSet:Set[RDD[(ImmutableBytesWritable, Result)]] = Set()
    import scala.collection.JavaConverters._
    ipcIds.asScala.toList.map(id => rddSet += scan2rdd(sc, ClusteringTable.TABLE_PEOPLECOMPARE, id+"_"+startDate, id+"_"+endDate))
    val unionData: RDD[(ImmutableBytesWritable, Result)] = sc.union(rddSet.toSeq)

    val featureData: RDD[(String, linalg.Vector)] = unionData.map(tuple2 => tuple2._2).map(result => {
      val rowKey = Bytes.toString(result.getRow)
      val feature = FaceObjectUtil.jsonToObject(Bytes.toString(result.getValue(ClusteringTable.PEOPELCOMPARE_COLUMNFAMILY, ClusteringTable.PEOPELCOMPARE_COLUMNDATA))).getAttribute.getFeature
      (rowKey, Vectors.dense(feature.map(_.toDouble)))
    }).cache()

    val dataCount = unionData.count()
    log.info("==========HBase " + ClusteringTable.TABLE_PEOPLECOMPARE + " dataCount : " + dataCount)
    if(dataCount <= 0) {
      log.error("==========HBase " + ClusteringTable.TABLE_PEOPLECOMPARE + " not data, kmeansClustering Exit!")
      return false
    }
    //KMeans训练
    val clusterMap: ConcurrentHashMap[Int, Int] = new ConcurrentHashMap[Int, Int]()
    val numClusters = if (clusterNum == null || "".equals(clusterNum)) Math.sqrt(dataCount.toDouble/2).toInt else clusterNum.toInt
    val numIterations = if (iteratorNum == null || "".equals(iteratorNum)) 10000 else iteratorNum.toInt
    val KMeansModel: KMeansModel = KMeans.train(featureData.map(_._2), numClusters, numIterations)
    clusteringMerger(clusterMap, KMeansModel, center_similarityThreshold)

    //排序，预测，过滤，替换，分组，过滤
    val lastResult = featureData.sortBy(tuple2=>tuple2._1.split("_").apply(1))
      .map(tuple2=>(KMeansModel.predict(tuple2._2), tuple2._1, cosineMeasure(KMeansModel.clusterCenters.apply(KMeansModel.predict(tuple2._2)).toArray, tuple2._2.toArray)))
      .filter(tuple3=>tuple3._3>similarityThreshold).map(tuple3=>(tuple3._1,tuple3._2))
      .map(tuple2=>(clusterMap.getOrDefault(tuple2._1, tuple2._1), tuple2._2))
      .groupBy(tuple2 => tuple2._1)
      .filter(tuple2=>tuple2._2.map(tuple2=>tuple2._2).toList.size >= appearCount+1)

    lastResult.foreachPartition { datas =>
      val dataList = new util.ArrayList[PeopleInAttribute]()
      val conn = ConnectionFactory.createConnection(hbaseConfig)
      val fromTable = conn.getTable(TableName.valueOf(ClusteringTable.TABLE_PEOPLECOMPARE)).asInstanceOf[HTable]

      for(data <- datas) {
        val list = new util.ArrayList[String]()
        val attribute = new PeopleInAttribute()
        val rowKeyList: Seq[String] = data._2.map(tuple2=>tuple2._2).toSeq
        val ftpUrl = FaceObjectUtil.jsonToObject(Bytes.toString(fromTable.get(getData(rowKeyList.head)).getValue(ClusteringTable.PEOPELCOMPARE_COLUMNFAMILY,ClusteringTable.PEOPELCOMPARE_COLUMNDATA))).getSurl

        list.addAll(rowKeyList.tail.asJava)
        attribute.setClusteringId(region + "-" + data._1.toString)
        attribute.setFtpUrl(ftpUrl)
        attribute.setFirstIpcId(rowKeyList.head.split("_").apply(0))
        attribute.setFirstAppearTime(rowKeyList.head.split("_").apply(1))
        attribute.setLastIpcId(rowKeyList.last.split("_").apply(0))
        attribute.setLastAppearTime(rowKeyList.last.split("_").apply(1))
        attribute.setCount(list.size())
        attribute.setFlag("no")
        attribute.setRowKeys(list)

        dataList.add(attribute)
      }

      log.info("==========clusteringData="+JSONUtil.toJson(dataList))
      val toTable = conn.getTable(TableName.valueOf(ClusteringTable.TABLE_ClUSTERINGINFO)).asInstanceOf[HTable]
      toTable.put(putData(startDate+"-"+region, ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES, JSONUtil.toJson(dataList)))
      fromTable.close()
      toTable.close()
    }
    return true

  }

  //相似度比较
  def cosineMeasure(v1: Array[Double], v2: Array[Double]): Double = {
    val member = v1.zip(v2).map(d => d._1 * d._2).sum
    val temp1 = math.sqrt(v1.map(num => {math.pow(num, 2)}).sum)
    val temp2 = math.sqrt(v2.map(num => {math.pow(num, 2)}).sum)
    val denominator = temp1 * temp2
    0.5 + 0.5 * (member / denominator)
  }

  //中心点合并
  def clusteringMerger(clusterMap: ConcurrentHashMap[Int, Int], KMeansModel: KMeansModel, center_similarityThreshold: Double): Unit = {
    val clusterIndexs = ListBuffer() ++ (0 until KMeansModel.clusterCenters.length)
    while(clusterIndexs.size > 0) {
      val head = clusterIndexs.head
      val tail = clusterIndexs.tail
      clusterMap.put(head, head)
      clusterIndexs -= head
      tail.map(x=> {
        if(cosineMeasure(KMeansModel.clusterCenters.apply(head).toArray, KMeansModel.clusterCenters.apply(x).toArray) > center_similarityThreshold) {
          clusterMap.put(x, head)
          clusterIndexs -= x
        }
      })
    }
  }
}