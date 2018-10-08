package com.hzgc.service.people.model;

import java.io.Serializable;

public class Flag implements Serializable {
    private Long id;

    private String peopleid;

    private Integer flagid;

    private String flag;

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

    public Integer getFlagid() {
        return flagid;
    }

    public void setFlagid(Integer flagid) {
        this.flagid = flagid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }
}