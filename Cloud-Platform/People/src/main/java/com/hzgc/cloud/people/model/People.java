package com.hzgc.cloud.people.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class People implements Serializable {
    private String id;

    private String name;

    private String idcard;

    private Long region;

    private String household;

    private String address;

    private String sex;

    private Integer age;

    private String birthday;

    private String politic;

    private String edulevel;

    private String job;

    private String birthplace;

    private Long community;

    private Integer important;

    private Integer care;

    private Imei imei;

    private Date lasttime;

    private Date createtime;

    private Date updatetime;

    private List<Flag> flag;

    private List<Imsi> imsi;

    private List<Phone> phone;

    private List<House> house;

    private List<Car> car;

    private Long pictureId;

    private List<PictureWithBLOBs> picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household == null ? null : household.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getPolitic() {
        return politic;
    }

    public void setPolitic(String politic) {
        this.politic = politic == null ? null : politic.trim();
    }

    public String getEdulevel() {
        return edulevel;
    }

    public void setEdulevel(String edulevel) {
        this.edulevel = edulevel == null ? null : edulevel.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace == null ? null : birthplace.trim();
    }

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public Integer getImportant() {
        return important;
    }

    public void setImportant(Integer important) {
        this.important = important;
    }

    public Integer getCare() {
        return care;
    }

    public void setCare(Integer care) {
        this.care = care;
    }

    public Imei getImei() {
        return imei;
    }

    public void setImei(Imei imei) {
        this.imei = imei;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public List<Flag> getFlag() {
        return flag;
    }

    public void setFlag(List<Flag> flag) {
        this.flag = flag;
    }

    public List<Imsi> getImsi() {
        return imsi;
    }

    public void setImsi(List<Imsi> imsi) {
        this.imsi = imsi;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<House> getHouse() {
        return house;
    }

    public void setHouse(List<House> house) {
        this.house = house;
    }

    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public List<PictureWithBLOBs> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureWithBLOBs> picture) {
        this.picture = picture;
    }
}