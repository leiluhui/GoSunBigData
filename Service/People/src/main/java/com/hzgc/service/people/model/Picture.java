package com.hzgc.service.people.model;

public class Picture {
    private Integer id;

    private Integer peopleid;

    private String idcardpicid;

    private String capturepicid;

    private String feature;

    private String bitfeature;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(Integer peopleid) {
        this.peopleid = peopleid;
    }

    public String getIdcardpicid() {
        return idcardpicid;
    }

    public void setIdcardpicid(String idcardpicid) {
        this.idcardpicid = idcardpicid == null ? null : idcardpicid.trim();
    }

    public String getCapturepicid() {
        return capturepicid;
    }

    public void setCapturepicid(String capturepicid) {
        this.capturepicid = capturepicid == null ? null : capturepicid.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }

    public String getBitfeature() {
        return bitfeature;
    }

    public void setBitfeature(String bitfeature) {
        this.bitfeature = bitfeature == null ? null : bitfeature.trim();
    }
}