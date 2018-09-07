package com.hzgc.service.people.model;

public class Imsi {
    private Integer id;

    private String imsiid;

    private String imsi;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImsiid() {
        return imsiid;
    }

    public void setImsiid(String imsiid) {
        this.imsiid = imsiid == null ? null : imsiid.trim();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }
}