package com.hzgc.common.carattribute.service;

import com.hzgc.common.carattribute.bean.CarAttribute;
import com.hzgc.common.carattribute.bean.CarAttributeValue;
import com.hzgc.common.carattribute.bean.CarLogistic;
import com.hzgc.seemmo.bean.carbean.CarData;
import com.hzgc.seemmo.service.ReadCarInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人/车属性查询
 */
public class CarAttributeService {

    /**
     * 车属性查询
     *
     * @return 属性对象列表
     */
    public List<CarAttribute> getCarAttribute() {
        List<CarAttribute> carAttributeList = new ArrayList<>();
        Map<String, Map<Integer, String>> vehicleMap = new ReadCarInfo().getVehicleMap();

        //对象类型
        CarAttribute objectType = new CarAttribute();
        objectType.setDesc("对象类型");
        objectType.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> objectTypeValueList = new ArrayList<>();
        Map<Integer, String> vehicle_object_type = vehicleMap.get(CarData.VEHICLE_BOJECT_TYPE);
        for (Integer key : vehicle_object_type.keySet()) {
            String value = vehicle_object_type.get(key);
            CarAttributeValue objectTypeValue = new CarAttributeValue();
            objectTypeValue.setDesc(value);
            objectTypeValue.setValue(key);
            objectTypeValueList.add(objectTypeValue);
        }
        objectType.setValues(objectTypeValueList);
        carAttributeList.add(objectType);

        // 车辆特征
        CarAttribute plateTypeCode = new CarAttribute();
        plateTypeCode.setDesc("车辆特征");
        plateTypeCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> plateTypeCodeValueList = new ArrayList<>();
        Map<Integer, String> plate_type_code = vehicleMap.get(CarData.PLATE_TYPE_CODE);
        for (Integer key : plate_type_code.keySet()) {
            String value = plate_type_code.get(key);
            CarAttributeValue plateTypeCodeValue = new CarAttributeValue();
            plateTypeCodeValue.setDesc(value);
            plateTypeCodeValue.setValue(key);
            plateTypeCodeValueList.add(plateTypeCodeValue);
        }
        plateTypeCode.setValues(plateTypeCodeValueList);
        carAttributeList.add(plateTypeCode);

        //车牌颜色
        CarAttribute plateColorCode = new CarAttribute();
        plateColorCode.setDesc("车牌颜色");
        plateColorCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> plateColorCodeValueList = new ArrayList<>();
        Map<Integer, String> plate_color_code = vehicleMap.get(CarData.PLATE_COLER_CODE);
        for (Integer key : plate_color_code.keySet()) {
            String value = plate_color_code.get(key);
            CarAttributeValue plateColorCodeValue = new CarAttributeValue();
            plateColorCodeValue.setDesc(value);
            plateColorCodeValue.setValue(key);
            plateColorCodeValueList.add(plateColorCodeValue);
        }
        plateColorCode.setValues(plateColorCodeValueList);
        carAttributeList.add(plateColorCode);

        //车牌标识
        CarAttribute plateFlagCode = new CarAttribute();
        plateFlagCode.setDesc("车牌状况");
        plateFlagCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> plateFlagCodeValueList = new ArrayList<>();
        Map<Integer, String> plate_flag_code = vehicleMap.get(CarData.PLATE_FLAG_CODE);
        for (Integer key : plate_flag_code.keySet()) {
            String value = plate_flag_code.get(key);
            CarAttributeValue plateFlagCodeValue = new CarAttributeValue();
            plateFlagCodeValue.setDesc(value);
            plateFlagCodeValue.setValue(key);
            plateFlagCodeValueList.add(plateFlagCodeValue);
        }
        plateFlagCode.setValues(plateFlagCodeValueList);
        carAttributeList.add(plateFlagCode);

        //车颜色
        CarAttribute vehicleColor = new CarAttribute();
        vehicleColor.setDesc("车颜色");
        vehicleColor.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> vehicleColorValueList = new ArrayList<>();
        Map<Integer, String> vehicle_color = vehicleMap.get(CarData.VEHICLE_COLOR);
        for (Integer key : vehicle_color.keySet()) {
            String value = vehicle_color.get(key);
            CarAttributeValue vehicleColorValue = new CarAttributeValue();
            vehicleColorValue.setDesc(value);
            vehicleColorValue.setValue(key);
            vehicleColorValueList.add(vehicleColorValue);
        }
        vehicleColor.setValues(vehicleColorValueList);
        carAttributeList.add(vehicleColor);

        //车辆类型
        CarAttribute vehicleType = new CarAttribute();
        vehicleType.setDesc("车辆类型");
        vehicleType.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> vehicleTypeValueList = new ArrayList<>();
        Map<Integer, String> vehicle_type = vehicleMap.get(CarData.VEHICLE_TYPE);
        for (Integer key : vehicle_type.keySet()) {
            String value = vehicle_type.get(key);
            CarAttributeValue vehicleTypeValue = new CarAttributeValue();
            vehicleTypeValue.setDesc(value);
            vehicleTypeValue.setValue(key);
            vehicleTypeValueList.add(vehicleTypeValue);
        }
        vehicleType.setValues(vehicleTypeValueList);
        carAttributeList.add(vehicleType);

        //车辆行驶方向
        CarAttribute mistakeCode = new CarAttribute();
        mistakeCode.setDesc("车辆行驶方向");
        mistakeCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> mistakeCodeValueList = new ArrayList<>();
        Map<Integer, String> mistake_code = vehicleMap.get(CarData.MISTAKE_CODE);
        for (Integer key : mistake_code.keySet()) {
            String value = mistake_code.get(key);
            CarAttributeValue vehicleTypeValue = new CarAttributeValue();
            vehicleTypeValue.setDesc(value);
            vehicleTypeValue.setValue(key);
            mistakeCodeValueList.add(vehicleTypeValue);
        }
        mistakeCode.setValues(mistakeCodeValueList);
        carAttributeList.add(mistakeCode);

        //天窗
        CarAttribute sunroofCode = new CarAttribute();
        sunroofCode.setDesc("天窗");
        sunroofCode.setLogistic(CarLogistic.AND);

        List<CarAttributeValue> sunroofCodeValueList = new ArrayList<>();
        Map<Integer, String> sunroof_code = vehicleMap.get(CarData.SUNROOF_CODE);
        for (Integer key : sunroof_code.keySet()) {
            String value = sunroof_code.get(key);
            CarAttributeValue vsunroofCodeValue = new CarAttributeValue();
            vsunroofCodeValue.setDesc(value);
            vsunroofCodeValue.setValue(key);
            sunroofCodeValueList.add(vsunroofCodeValue);
        }
        sunroofCode.setValues(sunroofCodeValueList);
        carAttributeList.add(sunroofCode);

        return carAttributeList;
    }
}
