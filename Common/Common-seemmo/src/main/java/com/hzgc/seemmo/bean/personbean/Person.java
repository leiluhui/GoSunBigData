package com.hzgc.seemmo.bean.personbean;

import lombok.Data;

import java.io.Serializable;

//行人对象
@Data
public class Person implements Serializable {

    //年龄
    private String age_code;
    //小孩类型
    private String baby_code;
    //拎东西
    private String bag_code;
    //下衣颜色
    private String bottomcolor_code;
    //下衣类型
    private String bottomtype_code;
    //帽子类型
    private String hat_code;
    //发型
    private String hair_code;
    //双肩包
    private String knapsack_code;
    //斜挎包
    private String messengerbag_code;
    //行人方向
    private String orientation_code;
    //性别
    private String sex_code;
    //单肩包
    private String shoulderbag_code;
    //雨伞
    private String umbrella_code;
    //上衣颜色
    private String uppercolor_code;
    //上衣类型
    private String uppertype_code;
    //骑车的类型
    private String car_type;
    //骑车的图片数据
    private byte[] car_data;
}
