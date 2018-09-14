package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PeopleVO implements Serializable {
    private String id;
    private String name;
    private String idCard;
    private String region;
    private String household;
    private String address;
    private String sex;
    private Integer age;
    private String birthday;
    private String politic;
    private String eduLevel;
    private String job;
    private String birthplace;
    private String community;
    private Integer important;
    private Integer care;
    private String lastTime;
    private String createTime;
    private String updateTime;
    private List<Integer> flag;
    private List<String> imsi;
    private List<String> phone;
    private List<String> house;
    private List<String> car;
    private byte[] picture;
    private List<byte[]> idcardPicture;
    private List<byte[]> capturePicture;
}
