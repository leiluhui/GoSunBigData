package com.hzgc.service.people.model;

import java.io.Serializable;

public class Imei implements Serializable {
    private Long id;

    private String peopleid;

    private String imei;

    private String guardianname;

    private String guardianphone;

    private String cadresname;

    private String cadresphone;

    private String policename;

    private String policephone;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getGuardianname() {
        return guardianname;
    }

    public void setGuardianname(String guardianname) {
        this.guardianname = guardianname == null ? null : guardianname.trim();
    }

    public String getGuardianphone() {
        return guardianphone;
    }

    public void setGuardianphone(String guardianphone) {
        this.guardianphone = guardianphone == null ? null : guardianphone.trim();
    }

    public String getCadresname() {
        return cadresname;
    }

    public void setCadresname(String cadresname) {
        this.cadresname = cadresname == null ? null : cadresname.trim();
    }

    public String getCadresphone() {
        return cadresphone;
    }

    public void setCadresphone(String cadresphone) {
        this.cadresphone = cadresphone == null ? null : cadresphone.trim();
    }

    public String getPolicename() {
        return policename;
    }

    public void setPolicename(String policename) {
        this.policename = policename == null ? null : policename.trim();
    }

    public String getPolicephone() {
        return policephone;
    }

    public void setPolicephone(String policephone) {
        this.policephone = policephone == null ? null : policephone.trim();
    }
}