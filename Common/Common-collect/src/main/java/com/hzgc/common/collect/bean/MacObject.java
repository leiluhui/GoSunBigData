package com.hzgc.common.collect.bean;

import java.io.Serializable;

public class MacObject implements Serializable{
//    private String id;                  //对象唯一ID
    private String sn; //设备Id
    private String mac;
    private Long time;
    private String communityId;
    private String wifisn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getWifisn() {
        return wifisn;
    }

    public void setWifisn(String wifisn) {
        this.wifisn = wifisn;
    }
}
