package com.hzgc.cloud.community.model;

public class ImsiAll {
    private Integer id;

    private String imsi;

    private String controlsn;

    private String sourcesn;

    private String imei;

    private String mscid;

    private String lac;

    private String cellid;

    private String freq;

    private String biscorpci;

    private String attach;

    private Long savetime;

    private String standard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getControlsn() {
        return controlsn;
    }

    public void setControlsn(String controlsn) {
        this.controlsn = controlsn == null ? null : controlsn.trim();
    }

    public String getSourcesn() {
        return sourcesn;
    }

    public void setSourcesn(String sourcesn) {
        this.sourcesn = sourcesn == null ? null : sourcesn.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getMscid() {
        return mscid;
    }

    public void setMscid(String mscid) {
        this.mscid = mscid == null ? null : mscid.trim();
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac == null ? null : lac.trim();
    }

    public String getCellid() {
        return cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid == null ? null : cellid.trim();
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq == null ? null : freq.trim();
    }

    public String getBiscorpci() {
        return biscorpci;
    }

    public void setBiscorpci(String biscorpci) {
        this.biscorpci = biscorpci == null ? null : biscorpci.trim();
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public Long getSavetime() {
        return savetime;
    }

    public void setSavetime(Long savetime) {
        this.savetime = savetime;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard == null ? null : standard.trim();
    }
}