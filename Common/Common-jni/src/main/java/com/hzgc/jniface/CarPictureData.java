package com.hzgc.jniface;

import java.io.Serializable;

public class CarPictureData implements Serializable {
    private String imageID;                          // 车辆大图ID
    private byte[] imageData;                       // 车辆大图
    private CarAttribute feature;       // 属性
    private int[] image_coordinate;

    public CarPictureData(){
    }

    public String getImageID() {
        return this.imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public CarAttribute getFeature() {
        return feature;
    }

    public void setFeature(CarAttribute feature) {
        this.feature = feature;
    }

    public int[] getImage_coordinate() {
        return image_coordinate;
    }

    public void setImage_coordinate(int[] image_coordinate) {
        this.image_coordinate = image_coordinate;
    }
}
