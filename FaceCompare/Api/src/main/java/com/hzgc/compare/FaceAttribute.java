package com.hzgc.compare;

import java.io.Serializable;

public class FaceAttribute implements Serializable {
    private float[] feature;
    private byte[] feature2;
    private int gender;
    private int huzi;
    private int eyeglasses;
    private int age;
    private int mask;
    private int sharpness;

    public FaceAttribute() {
    }

    public byte[] getFeature2() {
        return feature2;
    }

    public float[] getFeature() {
        return feature;
    }
}