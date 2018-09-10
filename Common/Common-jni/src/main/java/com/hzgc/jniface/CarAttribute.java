package com.hzgc.jniface;

import java.io.Serializable;

public class CarAttribute implements Serializable {
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

    public CarAttribute() {
    }

    public CarAttribute(String vehicle_object_type, String belt_maindriver, String belt_codriver, String brand_name, String call_code, String vehicle_color, String crash_code, String danger_code, String marker_code, String plate_schelter_code, String plate_flag_code, String plate_licence, String plate_destain_code, String plate_color_code, String plate_type_code, String rack_code, String sparetire_code, String mistake_code, String sunroof_code, String vehicle_type) {
        this.vehicle_object_type = vehicle_object_type;
        this.belt_maindriver = belt_maindriver;
        this.belt_codriver = belt_codriver;
        this.brand_name = brand_name;
        this.call_code = call_code;
        this.vehicle_color = vehicle_color;
        this.crash_code = crash_code;
        this.danger_code = danger_code;
        this.marker_code = marker_code;
        this.plate_schelter_code = plate_schelter_code;
        this.plate_flag_code = plate_flag_code;
        this.plate_licence = plate_licence;
        this.plate_destain_code = plate_destain_code;
        this.plate_color_code = plate_color_code;
        this.plate_type_code = plate_type_code;
        this.rack_code = rack_code;
        this.sparetire_code = sparetire_code;
        this.mistake_code = mistake_code;
        this.sunroof_code = sunroof_code;
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_object_type() {
        return vehicle_object_type;
    }

    public void setVehicle_object_type(String vehicle_object_type) {
        this.vehicle_object_type = vehicle_object_type;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCall_code() {
        return call_code;
    }

    public void setCall_code(String call_code) {
        this.call_code = call_code;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getCrash_code() {
        return crash_code;
    }

    public void setCrash_code(String crash_code) {
        this.crash_code = crash_code;
    }

    public String getDanger_code() {
        return danger_code;
    }

    public void setDanger_code(String danger_code) {
        this.danger_code = danger_code;
    }

    public String getMarker_code() {
        return marker_code;
    }

    public void setMarker_code(String marker_code) {
        this.marker_code = marker_code;
    }

    public String getPlate_schelter_code() {
        return plate_schelter_code;
    }

    public void setPlate_schelter_code(String plate_schelter_code) {
        this.plate_schelter_code = plate_schelter_code;
    }

    public String getPlate_flag_code() {
        return plate_flag_code;
    }

    public void setPlate_flag_code(String plate_flag_code) {
        this.plate_flag_code = plate_flag_code;
    }

    public String getPlate_licence() {
        return plate_licence;
    }

    public void setPlate_licence(String plate_licence) {
        this.plate_licence = plate_licence;
    }

    public String getPlate_destain_code() {
        return plate_destain_code;
    }

    public void setPlate_destain_code(String plate_destain_code) {
        this.plate_destain_code = plate_destain_code;
    }

    public String getPlate_color_code() {
        return plate_color_code;
    }

    public void setPlate_color_code(String plate_color_code) {
        this.plate_color_code = plate_color_code;
    }

    public String getPlate_type_code() {
        return plate_type_code;
    }

    public void setPlate_type_code(String plate_type_code) {
        this.plate_type_code = plate_type_code;
    }

    public String getRack_code() {
        return rack_code;
    }

    public void setRack_code(String rack_code) {
        this.rack_code = rack_code;
    }

    public String getMistake_code() {
        return mistake_code;
    }

    public void setMistake_code(String mistake_code) {
        this.mistake_code = mistake_code;
    }

    public String getSunroof_code() {
        return sunroof_code;
    }

    public void setSunroof_code(String sunroof_code) {
        this.sunroof_code = sunroof_code;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getBelt_maindriver() {
        return belt_maindriver;
    }

    public void setBelt_maindriver(String belt_maindriver) {
        this.belt_maindriver = belt_maindriver;
    }

    public String getBelt_codriver() {
        return belt_codriver;
    }

    public void setBelt_codriver(String belt_codriver) {
        this.belt_codriver = belt_codriver;
    }

    public String getSparetire_code() {
        return sparetire_code;
    }

    public void setSparetire_code(String sparetire_code) {
        this.sparetire_code = sparetire_code;
    }
}
