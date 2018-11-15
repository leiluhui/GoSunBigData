package com.hzgc.cluster.peoman.worker.model;

import lombok.Data;

import java.util.Date;

@Data
public class People {
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
    private Date lasttime;
    private Date createtime;
    private Date updatetime;
    private String phone;
    private String car;
    private String imsi;
    private Long pictureid;
}