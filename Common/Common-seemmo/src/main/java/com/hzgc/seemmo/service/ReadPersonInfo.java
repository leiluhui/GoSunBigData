package com.hzgc.seemmo.service;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ReadPersonInfo {

    private final Map<String, Map<Integer, String>> personMap = new HashMap<>();

    public ReadPersonInfo() {
        personMap.put("age_code", ReadPersonInfo.ageInfo());
        personMap.put("baby_code", ReadPersonInfo.babyInfo());
        personMap.put("bag_code", ReadPersonInfo.bagInfo());
        personMap.put("bottomcolor_code", ReadPersonInfo.bottomColorInfo());
        personMap.put("bottomtype_code", ReadPersonInfo.bottomTypeInfo());
        personMap.put("hat_code", ReadPersonInfo.hatInfo());
        personMap.put("hair_code", ReadPersonInfo.hairInfo());
        personMap.put("knapsack_code", ReadPersonInfo.knapsackInfo());
        personMap.put("messengerbag_code", ReadPersonInfo.messengerBagInfo());
        personMap.put("orientation_code", ReadPersonInfo.orientationInfo());
        personMap.put("sex_code", ReadPersonInfo.sexInfo());
        personMap.put("shoulderbag_code", ReadPersonInfo.shoulderBagInfo());
        personMap.put("umbrella_code", ReadPersonInfo.umbrellaInfo());
        personMap.put("uppercolor_code", ReadPersonInfo.upperColorInfo());
        personMap.put("uppertype_code", ReadPersonInfo.upperTypeInfo());
        personMap.put("car_type", ReadPersonInfo.objectTypeInfo());
    }

    private static Map<Integer, String> ageInfo() {
        HashMap<Integer, String> ageMap = new HashMap<>();
        ageMap.put(1, "儿童");
        ageMap.put(2, "青年");
        ageMap.put(3, "中年");
        ageMap.put(4, "老年");
        ageMap.put(5, "少年");
        return ageMap;
    }

    private static Map<Integer, String> babyInfo() {
        HashMap<Integer, String> babyMap = new HashMap<>();
        babyMap.put(1, "抱小孩");
        babyMap.put(2, "未抱小孩");
        babyMap.put(3, "背小孩");
        return babyMap;
    }

    private static Map<Integer, String> bagInfo() {
        HashMap<Integer, String> bagMap = new HashMap<>();
        bagMap.put(1, "未拎东西");
        bagMap.put(2, "拎东西");
        return bagMap;
    }

    private static Map<Integer, String> bottomColorInfo() {
        HashMap<Integer, String> bottomColorMap = new HashMap<>();
        bottomColorMap.put(1, "灰");
        bottomColorMap.put(2, "白");
        bottomColorMap.put(3, "红");
        bottomColorMap.put(4, "绿");
        bottomColorMap.put(5, "蓝");
        bottomColorMap.put(6, "黄");
        bottomColorMap.put(7, "黑");
        bottomColorMap.put(8, "紫");
        bottomColorMap.put(9, "深灰");
        return bottomColorMap;
    }

    private static Map<Integer, String> bottomTypeInfo() {
        HashMap<Integer, String> bottomTypeMap = new HashMap<>();
        bottomTypeMap.put(1, "长裤");
        bottomTypeMap.put(2, "短裤");
        bottomTypeMap.put(3, "七分裤");
        bottomTypeMap.put(4, "裙子");
        return bottomTypeMap;
    }

    private static Map<Integer, String> hatInfo() {
        HashMap<Integer, String> hatMap = new HashMap<>();
        hatMap.put(1, "未戴帽子");
        hatMap.put(2, "戴帽子");
        hatMap.put(3, "戴头盔");
        return hatMap;
    }

    private static Map<Integer, String> hairInfo() {
        HashMap<Integer, String> hairMap = new HashMap<>();
        hairMap.put(1, "长发");
        hairMap.put(2, "短发");
        hairMap.put(3, "马尾");
        hairMap.put(4, "头部被遮挡");
        hairMap.put(5, "盘发");
        return hairMap;
    }

    private static Map<Integer, String> knapsackInfo() {
        HashMap<Integer, String> knapsackMap = new HashMap<>();
        knapsackMap.put(1, "未背双肩包");
        knapsackMap.put(2, "双肩包(有包)");
        knapsackMap.put(3, "未确定");
        return knapsackMap;
    }

    private static Map<Integer, String> messengerBagInfo() {
        HashMap<Integer, String> messengerBagMap = new HashMap<>();
        messengerBagMap.put(1, "非斜挎包");
        messengerBagMap.put(2, "斜挎包");
        return messengerBagMap;
    }

    private static Map<Integer, String> orientationInfo() {
        HashMap<Integer, String> orientationMap = new HashMap<>();
        orientationMap.put(1, "前");
        orientationMap.put(2, "后");
        orientationMap.put(3, "侧");
        return orientationMap;
    }

    private static Map<Integer, String> sexInfo() {
        HashMap<Integer, String> sexMap = new HashMap<>();
        sexMap.put(1, "男");
        sexMap.put(2, "女");
        return sexMap;
    }

    private static Map<Integer, String> shoulderBagInfo() {
        HashMap<Integer, String> shoulderBagMap = new HashMap<>();
        shoulderBagMap.put(1, "非单肩包");
        shoulderBagMap.put(2, "单肩包");
        return shoulderBagMap;
    }

    private static Map<Integer, String> umbrellaInfo() {
        HashMap<Integer, String> umbrellaMap = new HashMap<>();
        umbrellaMap.put(1, "未打伞");
        umbrellaMap.put(2, "打伞");
        return umbrellaMap;
    }

    private static Map<Integer, String> upperColorInfo() {
        HashMap<Integer, String> upperColorMap = new HashMap<>();
        upperColorMap.put(1, "灰");
        upperColorMap.put(2, "白");
        upperColorMap.put(3, "红");
        upperColorMap.put(4, "绿");
        upperColorMap.put(5, "蓝");
        upperColorMap.put(6, "黄");
        upperColorMap.put(7, "黑");
        upperColorMap.put(8, "紫");
        upperColorMap.put(9, "深灰");
        return upperColorMap;
    }

    private static Map<Integer, String> objectTypeInfo() {
        HashMap<Integer, String> objectTypeMap = new HashMap<>();
        objectTypeMap.put(0, "未知类型");
        objectTypeMap.put(1, "行人");
        objectTypeMap.put(2, "自行车");
        objectTypeMap.put(3, "摩托车");
        objectTypeMap.put(4, "小汽车");
        objectTypeMap.put(5, "三轮车");
        objectTypeMap.put(6, "巴士");
        objectTypeMap.put(7, "面包车");
        objectTypeMap.put(8, "卡车");
        return objectTypeMap;
    }

    private static Map<Integer, String> upperTypeInfo() {
        HashMap<Integer, String> upperTypeMap = new HashMap<>();
        upperTypeMap.put(1, "长袖");
        upperTypeMap.put(2, "短袖");
        upperTypeMap.put(3, "羽绒服");
        upperTypeMap.put(4, "普通外套");
        upperTypeMap.put(5, "无外套");
        return upperTypeMap;
    }

    public Map<String, Map<Integer, String>> getPersonMap() {
        return personMap;
    }
}
