package com.hzgc.service.people.param;

import com.hzgc.service.people.model.Flag;
import com.hzgc.service.people.model.People;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台返回
 */
@ApiModel(value = "后台返回封装类")
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

    private List<String> flag;

    private String picture;

    private List<byte[]> capturePic;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;

    private String createTime;

    private String updateTime;

    public static PeopleVO peopleShift(People people) {
        PeopleVO peopleVO = new PeopleVO();
        if (people != null){
            peopleVO.setId(people.getId());
            peopleVO.setName(people.getName());
            peopleVO.setIdCard(people.getIdcard());
            //peopleVO.setRegion(people.getRegion());
            peopleVO.setHousehold(people.getHousehold());
            peopleVO.setAddress(people.getAddress());
            peopleVO.setSex(people.getSex());
            peopleVO.setAge(people.getAge());
            peopleVO.setBirthday(people.getBirthday());
            peopleVO.setPolitic(people.getPolitic());
            peopleVO.setEduLevel(people.getEdulevel());
            peopleVO.setJob(people.getJob());
            peopleVO.setBirthplace(people.getBirthplace());
            List<Integer> flags = people.getFlag();
            List<String> list = new ArrayList<>();
            for (Integer i : flags){
                list.add(com.hzgc.service.people.fields.Flag.getFlag(i));
            }
            peopleVO.setFlag(list);
            peopleVO.setImsi(people.getImsi());
            peopleVO.setPhone(people.getPhone());
            peopleVO.setHouse(people.getHouse());
            peopleVO.setCar(people.getCar());
            peopleVO.setPicture(people.getPicture().get(0));
        }
    }
}
