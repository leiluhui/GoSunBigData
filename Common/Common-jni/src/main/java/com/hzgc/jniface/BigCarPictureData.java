package com.hzgc.jniface;

import java.util.List;

public class BigCarPictureData extends CarPictureData {

    //大图检测，车辆特征对象
    private List<CarPictureData> smallImage;

    //车辆的个数
    private Integer carTotal;

    //图片类型
    private String imageType;

    public List <CarPictureData> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(List <CarPictureData> smallImage) {
        this.smallImage = smallImage;
    }

    public Integer getCarTotal() {
        return carTotal;
    }

    public void setCarTotal(Integer carTotal) {
        this.carTotal = carTotal;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
