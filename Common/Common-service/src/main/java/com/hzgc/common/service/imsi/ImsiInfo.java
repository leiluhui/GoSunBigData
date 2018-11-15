package com.hzgc.common.service.imsi;

import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.Serializable;

@Data
public class ImsiInfo implements Serializable {
    //uuid
    private String id;
    //设备id
    private String controlsn;
    //基站
    private String sourcesn;
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
    private long savetime;
    //格式时间
    private String time;
}