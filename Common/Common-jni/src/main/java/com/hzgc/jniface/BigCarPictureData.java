package com.hzgc.jniface;

import java.util.List;

public class BigCarPictureData extends CarPictureData {

    //大图检测，车辆特征对象
    private List<CarPictureData> smallImages;

    //车辆的个数
    private Integer total;

    //图片类型
    private String imageType;

    public List <CarPictureData> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List <CarPictureData> smallImages) {
        this.smallImages = smallImages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
