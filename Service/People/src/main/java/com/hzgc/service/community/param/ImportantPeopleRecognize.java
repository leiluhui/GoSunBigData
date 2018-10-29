package com.hzgc.service.community.param;

import com.hzgc.service.people.model.Car;
import com.hzgc.service.people.model.Flag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImportantPeopleRecognize implements Serializable {
    // 重点人员ID
    private String id;
    // 姓名
    private String name;
    // 身份证
    private String idCard;
    // 车辆列表
    private List<Car> car;
    // 标签列表
    private List<Flag> flag;
    // 最后抓拍时间
    private String lastTime;
    // 人员照片ID
    private Long peoplePictureId;
    // 被设别照片ID
    private Long pictureId;
    // 抓拍大图
    private String burl;
    // 抓拍小图
    private String surl;
    // 匹配相似度
    private Float similarity;
    // 抓拍时间
    private String captureTime;
    // 抓拍设备ID
    private String deviceId;
}
