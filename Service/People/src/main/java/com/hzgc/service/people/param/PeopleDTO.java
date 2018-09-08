package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 前端入参
 */
@ApiModel(value = "前端入参封装类")
@Data
public class PeopleDTO implements Serializable {

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

    private String job;

    private String birthplace;

    private Long community;

    private List<Integer> flag;

    private List<byte[]> idCardPic;

    private List<byte[]> capturePic;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;
}
