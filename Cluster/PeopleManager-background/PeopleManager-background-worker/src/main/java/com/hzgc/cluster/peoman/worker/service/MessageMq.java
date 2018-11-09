package com.hzgc.cluster.peoman.worker.service;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageMq {
    private String name;
    private String time;
    private String devId;
    private Integer age;
    private String sex;
    private String birthplace;
    private String idcard;
    private String address;
    private ArrayList<String> phone;
    private ArrayList<String> car;
    private ArrayList<String> mac;
    private ArrayList<Long> pictureid;
    private String surl;
    private String burl;
    private Long communtidy;
}
