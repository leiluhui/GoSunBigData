package com.hzgc.cluster.spark.clustering;


import com.hzgc.cluster.spark.util.PropertiesUtil;
import com.hzgc.common.facestarepo.table.alarm.RealNameServiceUtil;

import java.util.List;
import java.util.Properties;

public class MoveOut {
    public static void moveOut(String time){
        Properties properties = PropertiesUtil.getProperties();
        String jdbcUrl = properties.getProperty("job.moveOut.jdbcUrl");
        long updateTimeInterval = Long.parseLong(time) * 24 * 60 * 60 * 1000;
        RealNameServiceUtil realNameServiceUtil = new RealNameServiceUtil();
        List<String> idList = realNameServiceUtil.getOfflineAlarm(jdbcUrl,updateTimeInterval);
        for (String id : idList){
            realNameServiceUtil.upsertStatus(jdbcUrl,id);
        }
    }
}
