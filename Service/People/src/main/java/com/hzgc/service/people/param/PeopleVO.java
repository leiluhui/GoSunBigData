package com.hzgc.service.people.param;

import com.hzgc.service.people.model.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 后台返回
 */
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

    private List<Flag> flag;

    private List<Imsi> imsi;

    private List<Phone> phone;

    private List<House> house;

    private List<Car> car;

    private List<PictureWithBLOBs> idcardPicture;

    private List<PictureWithBLOBs> capturePicture;
}
