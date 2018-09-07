package com.hzgc.service.people.model;

public class Flag {
    private Integer id;

    private String flagid;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlagid() {
        return flagid;
    }

    public void setFlagid(String flagid) {
        this.flagid = flagid == null ? null : flagid.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}