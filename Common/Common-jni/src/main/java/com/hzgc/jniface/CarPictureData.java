package com.hzgc.jniface;

import java.io.Serializable;
import java.util.List;

public class CarPictureData implements Serializable {
    private String imageID;                          // 车辆大图ID
    private byte[] imageData;                        // 车辆大图
    private List<CarAttribute> attributeList;       // 属性

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

    public List<CarAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<CarAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
