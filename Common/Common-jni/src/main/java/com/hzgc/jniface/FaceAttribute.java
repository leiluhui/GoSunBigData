package com.hzgc.jniface;

import java.io.Serializable;
import java.util.Arrays;

public class FaceAttribute implements Serializable {

    //特征值
    private float[] feature;

    //bit类型的特征值
    private byte[] bitFeature;

    /**
     * 性别
     * 0女性
     * 1男性
     */
    private int gender;

    /**
     * 是否有胡子
     * 0没有胡子
     * 1有胡子
     */
    private int huzi;

    /*
    * 是否有口罩
    * 0没有口罩
    * 1有口罩
    * */
    private int mask;

    /**
     * 是否戴眼镜
     * 0没有眼镜
     * 1有眼镜
     */
    private int eyeglasses;

    //年龄
    private int age;

    /**
     * 清晰度评价,不清晰为0,清晰为1
     */
    private int sharpness;

    /**
     * 人脸图片坐标[x,y,width,height]
     */
    private int[] image_coordinate;

    public int getSharpness() {
        return sharpness;
    }

    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }

    public float[] getFeature() {
        return feature;
    }

    public void setFeature(float[] feature) {
        this.feature = feature;
    }

    public byte[] getBitFeature() {
        return bitFeature;
    }

    public void setBitFeature(byte[] bitFeature) {
        this.bitFeature = bitFeature;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHuzi() {
        return huzi;
    }

    public void setHuzi(int huzi) {
        this.huzi = huzi;
    }

    public int getEyeglasses() {
        return eyeglasses;
    }

    public void setEyeglasses(int eyeglasses) {
        this.eyeglasses = eyeglasses;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int[] getImage_coordinate() {
        return image_coordinate;
    }

    public void setImage_coordinate(int[] image_coordinate) {
        this.image_coordinate = image_coordinate;
    }

    @Override
    public String toString() {
        return "FaceAttribute{" +
                "feature=" + Arrays.toString(feature) +
                ", bitFeature=" + Arrays.toString(bitFeature) +
                ", gender=" + gender +
                ", huzi=" + huzi +
                ", mask=" + mask +
                ", eyeglasses=" + eyeglasses +
                ", age=" + age +
                ", sharpness=" + sharpness +
                ", image_coordinate=" + Arrays.toString(image_coordinate) +
                '}';
    }
}
