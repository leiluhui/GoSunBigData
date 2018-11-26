package com.hzgc.service.community.param;

import com.hzgc.service.people.model.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ImportantPeopleRecognizeHistory implements Serializable {
    // 人口库信息
    private String peopleId;    // 重点人员ID
    private String name;        // 姓名
    private String idcard;      // 身份证
    private String sex;         // 性别
    private Integer age;        // 年龄
    private String address;     // 现住地
    private String birthplace;  // 籍贯
    private List<Phone> phones; // 电话列表
    private List<Car> cars;     // 车辆列表
    private List<Imsi> imsis;   // IMSI列表
    // 抓拍识别记录信息
    private int type;           // 识别记录类型
    private Long pictureId;     // 被识别照片ID
    private String imsi;        // 被识别imis码
    private String plate;       // 被识别车牌
    private String burl;        // 抓拍大图
    private String surl;        // 抓拍小图
    private Timestamp captureTime;// 抓拍时间
    private Long community;     // 抓拍设备所在小区ID
    private String deviceId;    // 抓拍设备ID
}
