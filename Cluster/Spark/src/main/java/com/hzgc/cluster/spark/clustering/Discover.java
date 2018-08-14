package com.hzgc.cluster.spark.clustering;

import com.hzgc.cluster.spark.util.PropertiesUtil;
import com.hzgc.common.faceclustering.table.PeopleSchedulerTable;
import com.hzgc.common.faceclustering.table.PersonRegionTable;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.util.json.JSONUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class Discover {
    private static Logger LOG = Logger.getLogger(Discover.class);

    public static void main(String[] args) {
        Properties properties = PropertiesUtil.getProperties();
        String startDate = properties.getProperty("kmeans.start.date");
        String endDate = properties.getProperty("kmeans.end.date");
        Table table = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        Table regionTable = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME);
        Scan scan = new Scan();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.getActualMaximum(Calendar.DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = new Date().getTime();
        int startYear = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int endMonth = month + 1;
        int endYear = startYear;
        String startMonthStr;
        String endMonthStr;
        if (month < 10) {
            startMonthStr = "0" + month;
        } else {
            startMonthStr = "" + month;
        }
        if (month == 12) {
            endYear = startYear + 1;
            endMonth = 1;
        }
        if (endMonth < 10) {
            endMonthStr = "0" + endMonth;
        } else {
            endMonthStr = "" + endMonth;
        }
        if (startDate == null) {
            startDate = startYear + startMonthStr;
        }
        if (endDate == null) {
            endDate = endYear + endMonthStr;
        }
        try {
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                String moveInLastRunTime = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINLASTRUNTIME));
                String moveInCount = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINCOUNT));
                String moveOutDays = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEOUTDAYS));
                String regionId = Bytes.toString(result.getRow());
                Get get = new Get(Bytes.toBytes(regionId));
                Result result1 = regionTable.get(get);
                String ipcids = Bytes.toString(result1.getValue(PersonRegionTable.COLUMNFAMILY, PersonRegionTable.REGION_IPCIDS));
                List<String> ipcidList = JSONUtil.toObject(ipcids, ArrayList.class);
                long moveInLast = sdf.parse(moveInLastRunTime).getTime();
                long inter = day * 24 * 60 * 60 * 1000;
                if ((now - moveInLast) > inter){
                    KMeans2Clustering.kmeansClustering(regionId, ipcidList, Integer.parseInt(moveInCount), startDate, endDate);
                    LOG.info("The KMeans of " + regionId + " is succeed!!!");
                }
                MoveOut.moveOut(moveOutDays);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
