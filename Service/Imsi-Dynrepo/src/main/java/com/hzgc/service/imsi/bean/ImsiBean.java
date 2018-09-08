package com.hzgc.service.imsi.bean;

import lombok.Data;

/*
 * IMSI出参bean
 * */
@Data
public class ImsiBean {

    //手机IMSI号
    private String imsi;

    //时间
    private String time;

    //区域
    private String areaName;

    //小区
    private String plotName;

    //设备名称
    private String deviceName;

    //序列号
    private String sn;
}
