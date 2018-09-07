package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后台返回
 */
@Data
public class PeopleVO implements Serializable {
    private Long id;

    private String name;

    private String idCard;

    private Long region;

    private String household;

    private String address;

    private Integer sex;

    private Integer age;

    private Date birthday;

    private Integer politic;

    private Integer eduLevel;

    private Integer job;

    private String birthplace;

    private Long community;

    private List<Integer> flag;

    private List<byte[]> idCardPic;

    private List<byte[]> capturePic;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;

    private Date createTime;

    private Date updateTime;
}
