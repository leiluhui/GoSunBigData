package com.hzgc.service.imsi.model;

import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ImsiParam implements Serializable {
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    public static void main(String[] args) throws Exception{
        String time = "2018-11-17 15:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sdf.parse(time);
        System.out.printf("" + parse.getTime());
    }
}