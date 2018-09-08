package com.hzgc.service.imsi.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 * IMSI入参bean
 * */
@Data
public class ImsiParam implements Serializable {

    //手机IMSI号
    private String imsi;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //电围列表
    private List <String> sns;

    //开始条数
    private Integer start;

    //每页多少条
    private Integer limit;
}
