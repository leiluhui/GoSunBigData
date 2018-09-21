package com.hzgc.service.community.model;

public class CommunityDevice {
    private Long id;

    private Long community;

    private String deviceid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }
}