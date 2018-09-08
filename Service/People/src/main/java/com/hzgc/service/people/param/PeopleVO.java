package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后台返回
 */
@ApiModel(value = "后台返回封装类")
@Data
public class PeopleVO implements Serializable {
    private Long id;

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

    private List<String> flag;

    private List<byte[]> idCardPic;

    private List<byte[]> capturePic;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;

    private String createTime;

    private String updateTime;
}
