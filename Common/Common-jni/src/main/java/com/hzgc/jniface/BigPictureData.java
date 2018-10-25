package com.hzgc.jniface;

import java.util.List;

public class BigPictureData extends PictureData {

    //大图检测，人脸特征对象
    private List<PictureData> smallImages;

    //人脸的个数
    private Integer total;

    //图片类型
    private String imageType;

    public List <PictureData> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List <PictureData> smallImages) {
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
