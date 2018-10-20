package com.hzgc.service.dynrepo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 动态图片定义
 */
@Data
public class CapturedPicture implements Serializable {

    //小图url
    private String sabsolutepath;

    //大图url
    private String babsolutepath;

    //捕获照片的设备 id
    private String deviceId;


    private String deviceName;

    //图片的描述信息
    private String description;

    //图片的相似度
    private Float similarity;

    //性别
    private int gender;

    //年龄
    private int age;

    //胡子
    private int huzi;

    //口罩
    private int mask;

    //眼镜
    private int eyeglasses;

    //图片的附加信息，扩展预留
    private Map <String, Object> extend;

    //位置信息
    private String location;

    //时间戳
    @JsonProperty("time")
    private String timeStamp;

}
