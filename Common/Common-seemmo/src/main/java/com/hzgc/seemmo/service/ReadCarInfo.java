package com.hzgc.seemmo.service;

import java.util.HashMap;
import java.util.Map;

public class ReadCarInfo {

    private final Map<String, Map<Integer, String>> vehicleMap = new HashMap<>();

    public ReadCarInfo() {
        vehicleMap.put("vehicle_object_type", ReadCarInfo.objectTypeInfo());
        vehicleMap.put("belt_maindriver", ReadCarInfo.otherProperties());
        vehicleMap.put("belt_codriver", ReadCarInfo.otherProperties());
        vehicleMap.put("call_code", ReadCarInfo.otherProperties());
        vehicleMap.put("vehicle_color", ReadCarInfo.carColorInfo());
        vehicleMap.put("crash_code", ReadCarInfo.otherProperties());
        vehicleMap.put("danger_code", ReadCarInfo.otherProperties());
        vehicleMap.put("marker_code", ReadCarInfo.otherProperties());
        vehicleMap.put("plate_schelter_code", ReadCarInfo.otherProperties());
        vehicleMap.put("plate_flag_code", ReadCarInfo.plateFlagInfo());
        vehicleMap.put("plate_destain_code", ReadCarInfo.otherProperties());
        vehicleMap.put("plate_color_code", ReadCarInfo.plateColorInfo());
        vehicleMap.put("plate_type_code", ReadCarInfo.plateTypeInfo());
        vehicleMap.put("rack_code", ReadCarInfo.otherProperties());
        vehicleMap.put("sunroof_code", ReadCarInfo.otherProperties());
        vehicleMap.put("vehicle_type", ReadCarInfo.carKindInfo());
    }

    public static Map<Integer, String> objectTypeInfo() {
        HashMap<Integer, String> objectType = new HashMap<>();
        objectType.put(0, "未知类型");
        objectType.put(1, "行人");
        objectType.put(2, "自行车");
        objectType.put(3, "摩托车");
        objectType.put(4, "小汽车");
        objectType.put(5, "三轮车");
        objectType.put(6, "巴士");
        objectType.put(7, "面包车");
        objectType.put(8, "卡车");
        return objectType;
    }

    public static Map<Integer, String> plateTypeInfo() {
        HashMap<Integer, String> plateType = new HashMap<>();
        plateType.put(1, "黄牌(大型汽车)");
        plateType.put(2, "蓝牌(小型汽车)");
        plateType.put(3, "使馆");
        plateType.put(4, "领馆");
        plateType.put(6, "外籍");
        plateType.put(15, "挂车");
        plateType.put(16, "教练车");
        plateType.put(23, "警车");
        plateType.put(26, "港籍");
        plateType.put(27, "澳籍");
        plateType.put(31, "武警");
        plateType.put(32, "军车");
        plateType.put(99, "无法确定");
        return plateType;
    }

    public static Map<Integer, String> plateColorInfo() {
        HashMap<Integer, String> plateColor = new HashMap<>();
        plateColor.put(1, "黄");
        plateColor.put(2, "蓝");
        plateColor.put(3, "黑");
        plateColor.put(4, "白");
        return plateColor;
    }

    public static Map<Integer, String> plateFlagInfo() {
        HashMap<Integer, String> plateFlag = new HashMap<>();
        plateFlag.put(0, "空牌");
        plateFlag.put(1, "单牌");
        plateFlag.put(2, "双牌");
        plateFlag.put(3, "非车牌");
        plateFlag.put(4, "车牌太小");
        plateFlag.put(5, "车牌未检测到");
        return plateFlag;
    }

    public static Map<Integer, String> carColorInfo() {
        HashMap<Integer, String> carColor = new HashMap<>();
        carColor.put(1, "黑色");
        carColor.put(2, "蓝色");
        carColor.put(3, "棕色");
        carColor.put(4, "绿色");
        carColor.put(5, "灰色");
        carColor.put(6, "白色");
        carColor.put(7, "红色");
        carColor.put(8, "黄色");
        carColor.put(9, "粉色");
        carColor.put(10, "紫色");
        carColor.put(11, "青色");
        carColor.put(12, "深灰色");
        carColor.put(13, "金色");
        return carColor;
    }

    public static Map<Integer, String> carKindInfo() {
        HashMap<Integer, String> carKind = new HashMap<>();
        carKind.put(0, "其他");
        carKind.put(1, "轿车");
        carKind.put(2, "越野车");
        carKind.put(3, "商务车");
        carKind.put(4, "小型货车");
        carKind.put(5, "大型货车");
        carKind.put(6, "轻客");
        carKind.put(7, "小型客车");
        carKind.put(8, "大型客车");
        carKind.put(9, "三轮车");
        carKind.put(10, "微面");
        carKind.put(11, "皮卡车");
        carKind.put(12, "挂车");
        carKind.put(13, "混凝土搅拌车");
        carKind.put(14, "罐车");
        carKind.put(15, "随车吊");
        carKind.put(16, "消防车");
        carKind.put(17, "渣土车");
        carKind.put(18, "押运车");
        carKind.put(19, "工程抢修车");
        carKind.put(20, "救援车");
        carKind.put(21, "栏板卡车");
        return carKind;
    }

    public static Map<Integer, String> mistakeInfo() {
        HashMap<Integer, String> mistake = new HashMap<>();
        mistake.put(0, "front");
        mistake.put(1, "back");
        mistake.put(3, "side");
        return mistake;
    }

    public static Map<Integer, String> otherProperties() {
        HashMap<Integer, String> otherPropertis = new HashMap<>();
        otherPropertis.put(1, "无");
        otherPropertis.put(2, "有");
        return otherPropertis;
    }

    public Map<String, Map<Integer, String>> getVehicleMap() {
        return vehicleMap;
    }
}
