package com.hzgc.jniface;

import java.util.List;

public class BigPictureData extends PictureData {

    //大图检测，人脸特征对象
    private List<PictureData> smallImage;

    //人脸的个数
    private Integer faceTotal;

    //图片类型
    private String imageType;

    public List<PictureData> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(List <PictureData> smallImage) {
        this.smallImage = smallImage;
    }

    public Integer getFaceTotal() {
        return faceTotal;
    }

    public void setFaceTotal(Integer faceTotal) {
        this.faceTotal = faceTotal;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
