package com.hzgc.service.people.model;

public class Imsi {
    private Long id;

    private Long imsiid;

    private String imsi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImsiid() {
        return imsiid;
    }

    public void setImsiid(Long imsiid) {
        this.imsiid = imsiid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }
}