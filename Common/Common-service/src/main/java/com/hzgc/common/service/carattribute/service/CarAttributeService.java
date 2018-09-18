package com.hzgc.common.service.carattribute.service;

import com.hzgc.common.service.carattribute.bean.CarAttribute;
import com.hzgc.common.service.carattribute.bean.CarAttributeValue;
import com.hzgc.common.service.carattribute.bean.CarLogistic;
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

        /*//车辆行驶方向
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
        carAttributeList.add(mistakeCode);*/

        /*//天窗
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
        carAttributeList.add(sunroofCode);*/

        //主驾驶安全带
        CarAttribute belt_maindriverCode = new CarAttribute();
        belt_maindriverCode.setDesc("主驾驶安全带");
        belt_maindriverCode.setLogistic(CarLogistic.AND);

        List<CarAttributeValue> belt_maindriverCodeValueList = new ArrayList<>();
        Map<Integer, String> belt_maindriver_code = vehicleMap.get(CarData.BELT_MAINDRIVER);
        for (Integer key : belt_maindriver_code.keySet()) {
            String value = belt_maindriver_code.get(key);
            CarAttributeValue belt_maindriverCodeValue = new CarAttributeValue();
            belt_maindriverCodeValue.setDesc(value);
            belt_maindriverCodeValue.setValue(key);
            belt_maindriverCodeValueList.add(belt_maindriverCodeValue);
        }
        belt_maindriverCode.setValues(belt_maindriverCodeValueList);
        carAttributeList.add(belt_maindriverCode);

        //副驾驶安全带
        CarAttribute belt_codriverCode = new CarAttribute();
        belt_codriverCode.setDesc("副驾驶安全带");
        belt_codriverCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> belt_codriverValueList = new ArrayList<>();
        Map<Integer, String> belt_codriver_code = vehicleMap.get(CarData.BELT_CODRIVER);
        for (Integer key : belt_codriver_code.keySet()) {
            String value = belt_codriver_code.get(key);
            CarAttributeValue belt_codriverCodeValue = new CarAttributeValue();
            belt_codriverCodeValue.setDesc(value);
            belt_codriverCodeValue.setValue(key);
            belt_codriverValueList.add(belt_codriverCodeValue);
        }
        belt_codriverCode.setValues(belt_codriverValueList);
        carAttributeList.add(belt_codriverCode);

        //是否打电话
        CarAttribute callCode = new CarAttribute();
        callCode.setDesc("是否打电话");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> callCodeValueList = new ArrayList<>();
        Map<Integer, String> call_code = vehicleMap.get(CarData.CALL_CODE);
        for (Integer key : call_code.keySet()) {
            String value = call_code.get(key);
            CarAttributeValue callCodeValue = new CarAttributeValue();
            callCodeValue.setDesc(value);
            callCodeValue.setValue(key);
            callCodeValueList.add(callCodeValue);
        }
        callCode.setValues(callCodeValueList);
        carAttributeList.add(callCode);

        //是否撞损
        CarAttribute crashCode = new CarAttribute();
        callCode.setDesc("是否撞损");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> crashCodeValueList = new ArrayList<>();
        Map<Integer, String> crash_code = vehicleMap.get(CarData.CRASH_CODE);
        for (Integer key : crash_code.keySet()) {
            String value = crash_code.get(key);
            CarAttributeValue crashCodeValue = new CarAttributeValue();
            crashCodeValue.setDesc(value);
            crashCodeValue.setValue(key);
            crashCodeValueList.add(crashCodeValue);
        }
        crashCode.setValues(crashCodeValueList);
        carAttributeList.add(crashCode);

        //危化品车
        CarAttribute dangerCode = new CarAttribute();
        callCode.setDesc("危化品车");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> dangerCodeValueList = new ArrayList<>();
        Map<Integer, String> danger_code = vehicleMap.get(CarData.DANGER_CODE);
        for (Integer key : danger_code.keySet()) {
            String value = danger_code.get(key);
            CarAttributeValue dangerCodeValue = new CarAttributeValue();
            dangerCodeValue.setDesc(value);
            dangerCodeValue.setValue(key);
            dangerCodeValueList.add(dangerCodeValue);
        }
        dangerCode.setValues(dangerCodeValueList);
        carAttributeList.add(dangerCode);

        //车牌污损
        CarAttribute plateSchelterCode = new CarAttribute();
        callCode.setDesc("车牌污损");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> plateSchelterCodeValueList = new ArrayList<>();
        Map<Integer, String> plate_schelter_code = vehicleMap.get(CarData.PLATE_SCHELTER_CODE);
        for (Integer key : plate_schelter_code.keySet()) {
            String value = plate_schelter_code.get(key);
            CarAttributeValue plateSchelterCodeValue = new CarAttributeValue();
            plateSchelterCodeValue.setDesc(value);
            plateSchelterCodeValue.setValue(key);
            plateSchelterCodeValueList.add(plateSchelterCodeValue);
        }
        plateSchelterCode.setValues(plateSchelterCodeValueList);
        carAttributeList.add(plateSchelterCode);

        //车牌遮挡
        CarAttribute plateDestainCode = new CarAttribute();
        callCode.setDesc("车牌遮挡");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> plateDestainCodeValueList = new ArrayList<>();
        Map<Integer, String> plate_destain_code = vehicleMap.get(CarData.PLATE_DESTAIN_CODE);
        for (Integer key : plate_destain_code.keySet()) {
            String value = plate_destain_code.get(key);
            CarAttributeValue plateDestainCodeValue = new CarAttributeValue();
            plateDestainCodeValue.setDesc(value);
            plateDestainCodeValue.setValue(key);
            plateDestainCodeValueList.add(plateDestainCodeValue);
        }
        plateDestainCode.setValues(plateDestainCodeValueList);
        carAttributeList.add(plateDestainCode);

        //行李架
        CarAttribute rackCode = new CarAttribute();
        callCode.setDesc("行李架");
        callCode.setLogistic(CarLogistic.AND);
        List<CarAttributeValue> rackCodeValueList = new ArrayList<>();
        Map<Integer, String> rack_code = vehicleMap.get(CarData.RACK_CODE);
        for (Integer key : rack_code.keySet()) {
            String value = rack_code.get(key);
            CarAttributeValue rackCodeValue = new CarAttributeValue();
            rackCodeValue.setDesc(value);
            rackCodeValue.setValue(key);
            rackCodeValueList.add(rackCodeValue);
        }
        rackCode.setValues(rackCodeValueList);
        carAttributeList.add(rackCode);


        return carAttributeList;
    }
}
