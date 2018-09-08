package com.hzgc.service.people.model;

public class Picture {
    private Long id;

    private Long peopleid;

    private Long idcardpicid;

    private Long capturepicid;

    private String feature;

    private String bitfeature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(Long peopleid) {
        this.peopleid = peopleid;
    }

    public Long getIdcardpicid() {
        return idcardpicid;
    }

    public void setIdcardpicid(Long idcardpicid) {
        this.idcardpicid = idcardpicid;
    }

    public Long getCapturepicid() {
        return capturepicid;
    }

    public void setCapturepicid(Long capturepicid) {
        this.capturepicid = capturepicid;
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