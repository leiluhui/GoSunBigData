package com.hzgc.cluster.dispatch.producer;

import lombok.Data;

@Data
public class AlarmMessage {
    private String deviceId;
    private String deviceName; // 设备名称
    private Integer type; // 0：人脸识别，1：车辆识别，2：MAC识别
    private float sim;  //相似度
    private String name; //姓名
    private String mac;
    private String plate; //车牌号
    private String idCard;
    private String captureImage; //抓拍图
    private String id; //布控图片
    private String time; // 抓拍时间
    private String bCaptureImage; //抓拍大图
    private String notes; //备注
}
