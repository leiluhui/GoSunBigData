package com.hzgc.cluster.dispatch.model;

public class DispatchDefinition {
    private String id;

    private Long region;

    private String designation;

    private Integer status;

    private String ipcs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation == null ? null : designation.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIpcs() {
        return ipcs;
    }

    public void setIpcs(String ipcs) {
        this.ipcs = ipcs == null ? null : ipcs.trim();
    }
}