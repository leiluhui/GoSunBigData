package com.hzgc.service.people.model;

import java.util.Date;

public class People {
    private Integer id;

    private String name;

    private String idcard;

    private Long region;

    private String household;

    private String address;

    private Integer sex;

    private Integer age;

    private Date birthday;

    private Integer politic;

    private Integer edulevel;

    private Integer job;

    private Integer birthplace;

    private Long community;

    private String flag;

    private String idcardpic;

    private String capturepic;

    private String imsi;

    private String phone;

    private String house;

    private String car;

    private Date createtime;

    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getPolitic() {
        return politic;
    }

    public void setPolitic(Integer politic) {
        this.politic = politic;
    }

    public Integer getEdulevel() {
        return edulevel;
    }

    public void setEdulevel(Integer edulevel) {
        this.edulevel = edulevel;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(Integer birthplace) {
        this.birthplace = birthplace;
    }

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getIdcardpic() {
        return idcardpic;
    }

    public void setIdcardpic(String idcardpic) {
        this.idcardpic = idcardpic == null ? null : idcardpic.trim();
    }

    public String getCapturepic() {
        return capturepic;
    }

    public void setCapturepic(String capturepic) {
        this.capturepic = capturepic == null ? null : capturepic.trim();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house == null ? null : house.trim();
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car == null ? null : car.trim();
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
}