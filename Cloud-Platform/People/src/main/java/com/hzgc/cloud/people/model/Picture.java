package com.hzgc.cloud.people.model;

import java.io.Serializable;

public class Picture implements Serializable {
    private Long id;

    private String peopleid;

    private String feature;

    private String bitfeature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(String peopleid) {
        this.peopleid = peopleid == null ? null : peopleid.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }

    public String getBitfeature() {
        return bitfeature;
    }

    public void setBitfeature(String bitfeature) {
        this.bitfeature = bitfeature == null ? null : bitfeature.trim();
    }
}