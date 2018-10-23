package com.hzgc.jniface;

import java.io.Serializable;

public class CarPictureData implements Serializable {
    private String imageID;                          // 车辆大图ID
    private byte[] imageData;                       // 车辆大图
    private CarAttribute carAttribute;       // 属性

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

    public CarAttribute getCarAttribute() {
        return carAttribute;
    }

    public void setCarAttribute(CarAttribute carAttribute) {
        this.carAttribute = carAttribute;
    }

}
