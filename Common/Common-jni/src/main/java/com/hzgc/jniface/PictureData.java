package com.hzgc.jniface;


import java.io.Serializable;
import java.util.List;

public class PictureData implements Serializable {
    //图片ID
    private String imageID;

    //图片二进制数据
    private byte[] imageData;

    //人脸特征对象,包括特征值和人脸属性
    private FaceAttribute feature;

    //大图检测，人脸特征对象
    private List<SmallImage> smallImage;

    //人脸的个数
    private Integer faceTotal;

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public FaceAttribute getFeature() {
        return feature;
    }

    public void setFeature(FaceAttribute feature) {
        this.feature = feature;
    }

    public List <SmallImage> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(List <SmallImage> smallImage) {
        this.smallImage = smallImage;
    }

    public Integer getFaceTotal() {
        return faceTotal;
    }

    public void setFaceTotal(Integer faceTotal) {
        this.faceTotal = faceTotal;
    }
}
