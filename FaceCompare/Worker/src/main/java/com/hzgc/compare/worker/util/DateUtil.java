package com.hzgc.compare.worker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static List<String> getPeriod(String start, String end, int daysPerThread) throws ParseException {
        List<String> list = new ArrayList<>();
        long startTime = sdf.parse(start).getTime();
        long endTime = sdf.parse(end).getTime();
        String time1 = start;
        while (startTime < endTime){
            startTime += 1000L * 60 * 60 * 24 * (daysPerThread - 1);
            startTime = startTime < endTime ? startTime : endTime;
            String time2 = sdf.format(new Date(startTime));
            list.add(time1 + "," + time2);
            startTime += 1000L * 60 * 60 * 24;
            time1 = sdf.format(new Date(startTime));
        }
        return list;
    }

    /**
     * 获取本月和上一个月
     * @return
     */
    public static List<String> getMonthes(){
        List<String> res = new ArrayList<>();
        //得到本月和上月
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String ym = sdf.format(date);
        String [] strings = ym.split("-");
        Integer m = Integer.valueOf(strings[1]) - 1;
        String lastMonth = null;
        if (m > 0 && m < 10){
            lastMonth = strings[0] + "-0" + m;
        }
        if (m == 0) {
            int year = Integer.valueOf(strings[0]) - 1;
            lastMonth = String.valueOf(year) + "-" + String.valueOf(12);
        }
        long start = System.currentTimeMillis();
        res.add(lastMonth);
        res.add(ym);
        return res;
    }
}
