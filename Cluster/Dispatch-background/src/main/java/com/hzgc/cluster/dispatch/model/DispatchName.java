package com.hzgc.cluster.dispatch.model;

public class DispatchName {
    private String defid;

    private String name;

    private String feature;

    private String bitFeature;

    private byte[] picture;

    public String getDefid() {
        return defid;
    }

    public void setDefid(String defid) {
        this.defid = defid == null ? null : defid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }

    public String getBitFeature() {
        return bitFeature;
    }

    public void setBitFeature(String bitFeature) {
        this.bitFeature = bitFeature == null ? null : bitFeature.trim();
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}