package com.hzgc.service.community.model;

import java.util.Date;

public class RecognizeRecord {
    private String id;

    private String peopleid;

    private Long community;

    private Long pictureid;

    private String deviceid;

    private Date capturetime;

    private String surl;

    private String burl;

    private Integer flag;

    private Float similarity;

    private String plate;

    private Integer type;

    private String imsi;

    private String mac;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(String peopleid) {
        this.peopleid = peopleid == null ? null : peopleid.trim();
    }

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public Long getPictureid() {
        return pictureid;
    }

    public void setPictureid(Long pictureid) {
        this.pictureid = pictureid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }

    public Date getCapturetime() {
        return capturetime;
    }

    public void setCapturetime(Date capturetime) {
        this.capturetime = capturetime;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl == null ? null : surl.trim();
    }

    public String getBurl() {
        return burl;
    }

    public void setBurl(String burl) {
        this.burl = burl == null ? null : burl.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate == null ? null : plate.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }
}