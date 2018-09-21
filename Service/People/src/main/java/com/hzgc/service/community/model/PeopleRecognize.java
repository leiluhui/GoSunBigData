package com.hzgc.service.community.model;

import java.util.Date;

public class PeopleRecognize {
    private Long id;

    private String peopleid;

    private Long pictureid;

    private String deviceid;

    private Date capturetime;

    private String surl;

    private String burl;

    private Integer flag;

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
}