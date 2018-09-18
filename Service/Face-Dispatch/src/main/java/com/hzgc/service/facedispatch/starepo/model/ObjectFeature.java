package com.hzgc.service.facedispatch.starepo.model;

import com.hzgc.common.util.json.JacksonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectFeature implements Serializable, Comparable<ObjectFeature> {
    private String id;                          // 对象id
    private String typeId;                      // 对象类型id
    private float[] feature;                    // 特征值
    private float[] bitfea;                     // 特征值
    private float  similarity;                  // 相似度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public float[] getFeature() {
        return feature;
    }

    public void setFeature(float[] feature) {
        this.feature = feature;
    }

    public float[] getBitfea() {
        return bitfea;
    }

    public void setBitfea(float[] bitfea) {
        this.bitfea = bitfea;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "ObjectFeature{" +
                "id='" + id + '\'' +
                ", typeId='" + typeId + '\'' +
                ", feature=" + Arrays.toString(feature) +
                ", bitfea=" + Arrays.toString(bitfea) +
                ", similarity=" + similarity +
                '}';
    }

    @Override
    public int compareTo(ObjectFeature feature) {
        float i = feature.getSimilarity() - this.getSimilarity();
        return (int) i;
    }

    public static void main(String[] args) {
        ObjectFeature feature1 = new ObjectFeature();
        feature1.setSimilarity(50.5F);
        ObjectFeature feature2 = new ObjectFeature();
        feature2.setSimilarity(20.5F);
        ObjectFeature feature3 = new ObjectFeature();
        feature3.setSimilarity(80.0F);
        List<ObjectFeature> list = new ArrayList<>();
        list.add(feature1);
        list.add(feature2);
        list.add(feature3);
        System.out.println(JacksonUtil.toJson(list));
        Collections.sort(list);
        System.out.println(JacksonUtil.toJson(list));
    }
}
