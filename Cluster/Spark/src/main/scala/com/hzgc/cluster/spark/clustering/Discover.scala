package com.hzgc.cluster.spark.clustering

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date}

import com.hzgc.cluster.spark.util.PropertiesUtil
import com.hzgc.common.faceclustering.table.{PeopleSchedulerTable, PersonRegionTable}
import com.hzgc.common.facestarepo.table.alarm.ResidentUtil
import com.hzgc.common.hbase.HBaseHelper
import com.hzgc.common.util.json.JSONUtil
import org.apache.hadoop.hbase.client.{Get, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession


object Discover extends Serializable {

  val LOG: Logger = Logger.getLogger(Discover.getClass)

  def main(args: Array[String]): Unit = {
    val properties = PropertiesUtil.getProperties
    var startDate = properties.getProperty("kmeans.start.date")
    var endDate = properties.getProperty("kmeans.end.date")
    val appName = properties.getProperty("job.discover.appName")
    val sc = SparkSession.builder().appName(appName).getOrCreate().sparkContext
    val jdbcUrlBroadCast = sc.broadcast(properties.getProperty("phoenix.jdbc.url"))
    val schedulerTable = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME)
    val regionTable = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME)
    val scan = new Scan()
    val calendar = Calendar.getInstance()
    val format = new SimpleDateFormat("yyyyMM")
    calendar.setTime(new Date())
    calendar.add(Calendar.MONTH,-1)
    val date = calendar.getTime
    calendar.setTime(format.parse(format.format(date)))
    val day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    LOG.info("Interval day is : " + day)
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val now = new Date().getTime
    val startYear = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    var endMonth = month + 1
    var endYear = startYear
    var startMonthStr = ""
    var endMonthStr = ""
    if (month < 10) startMonthStr = "0" + month
    else startMonthStr = "" + month
    if (month == 12) {
      endYear = startYear + 1
      endMonth = 1
    }
    if (endMonth < 10) endMonthStr = "0" + endMonth
    else endMonthStr = "" + endMonth
    if (startDate == null) startDate = startYear + startMonthStr
    if (endDate == null) endDate = endYear + endMonthStr
    val resultScanner = schedulerTable.getScanner(scan)
    val iterator = resultScanner.iterator()
    if (iterator.hasNext){
      val result = iterator.next()
      val moveInLastRunTime = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY,PeopleSchedulerTable.MOVEINLASTRUNTIME))
      val moveInCount = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY,PeopleSchedulerTable.MOVEINCOUNT))
      val moveOutDays = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY,PeopleSchedulerTable.MOVEOUTDAYS))
      val regionID = Bytes.toString(result.getRow)
      val get = new Get(Bytes.toBytes(regionID))
      val result1 = regionTable.get(get)
      val ipcids = Bytes.toString(result1.getValue(PersonRegionTable.COLUMNFAMILY,PersonRegionTable.REGION_IPCIDS))
      val ipcidList = JSONUtil.toObject(ipcids,new util.ArrayList[String].getClass)
      val moveInLast = simpleDateFormat.parse(moveInLastRunTime).getTime
      val inter = day * 24 * 60 * 60 * 1000
      if ((now - moveInLast) > inter){
        val success = KMeans2Clustering.kmeansClustering(regionID,ipcidList,Integer.parseInt(moveInCount),startDate,endDate,sc)
        if(success){
          LOG.info("The kmeans is successfully!!!")
        }else{
          LOG.info("The kmeans is filed!!!")
        }
      }
      val updateTimeInterval = moveOutDays.toLong * 24 * 60 * 60 * 1000
      ResidentUtil.getInstance(jdbcUrlBroadCast.value).getOfflineAlarmAndUpsertStatus(updateTimeInterval)
    }
    sc.stop()
  }
}













