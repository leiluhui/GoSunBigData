package com.hzgc.cluster.dispach.cache;

//import lombok.Data;

//@Data
public class DispachData {
    private String id;
    private String bitfeature;
    private Integer index;
    private String car;
    private String mac;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBitfeature() {
        return bitfeature;
    }

    public void setBitfeature(String bitfeature) {
        this.bitfeature = bitfeature;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    @Override
    public String toString() {
        return "DispachData{" +
                "id='" + id + '\'' +
                ", bitfeature='" + bitfeature + '\'' +
                ", index=" + index +
                ", car='" + car + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
