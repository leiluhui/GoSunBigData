package com.hzgc.common.service.imsi;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImsiInfo implements Serializable {
    private static final long serialVersionUID = 7927252429337935830L;
    //基站序列号
    private String sn;
    //手机IMSI码
    private String imsi;
    //小区ID
    private String cellid;
    //区域码
    private String lac;
    //MSC编号
    private String mscid;
    //手机的IMEI编号
    private String imei;
    //频点
    private String freq;
    //小区识别
    private String biscorpci;
    //通道编号
    private String attach;
    //运营商制式
    private String standard;
    //时间
    private Long savetime;
    //开始时间
    private Long startTime;
    //结束时间
    private Long endTime;
}