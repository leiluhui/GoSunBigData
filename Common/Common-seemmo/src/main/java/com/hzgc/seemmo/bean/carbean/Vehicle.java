package com.hzgc.seemmo.bean.carbean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class Vehicle implements Serializable {

    //车辆对象类型
    private String vehicle_object_type;
    //主驾驶安全带
    private String belt_maindriver;
    //副驾驶安全带
    private String belt_codriver;
    //车的商标
    private String brand_name;
    //打电话
    private String call_code;
    //车辆颜色
    private String vehicle_color;
    //撞损
    private String crash_code;
    //危化品车
    private String danger_code;
    //小物品检测
    private String marker_code;
    //车牌污损
    private String plate_schelter_code;
    //车牌标识
    private String plate_flag_code;
    //车牌号
    private String plate_licence;
    //车牌是否遮挡
    private String plate_destain_code;
    //车牌颜色
    private String plate_color_code;
    //车牌类型
    private String plate_type_code;
    //行李架
    private String rack_code;
    //备用轮胎
    private String sparetire_code;
    //车辆行驶方向
    private String mistake_code;
    //天窗
    private String sunroof_code;
    //车辆类型
    private String vehicle_type;
    //车辆图片数据
    private byte[] vehicle_data;
}
