package com.hzgc.cluster.dispatch.consumer;

//import lombok.Data;

import java.io.Serializable;

//@Data
public class KafkaMessage implements Serializable {
    private String id;
    private Long regionId;
    private String bitFeature;
    private String car;
    private String mac;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getBitFeature() {
        return bitFeature;
    }

    public void setBitFeature(String bitFeature) {
        this.bitFeature = bitFeature;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}