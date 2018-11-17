package com.hzgc.service.imsi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MacParam implements Serializable{

    //电围设备
    private List<String> list;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //起始条数
    private int start;
    //分页条数
    private int limit;
}
