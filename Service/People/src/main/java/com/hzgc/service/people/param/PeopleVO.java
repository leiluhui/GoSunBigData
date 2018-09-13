package com.hzgc.service.people.param;

import com.hzgc.service.people.model.People;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private List<String> flag;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;

    private List<Long> pictureIds;

    public static PeopleVO peopleShift(People people) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PeopleVO peopleVO = new PeopleVO();
        if (people != null){
            peopleVO.setId(people.getId());
            peopleVO.setName(people.getName());
            peopleVO.setIdCard(people.getIdcard());
            peopleVO.setRegion(String.valueOf(people.getRegion()));
            peopleVO.setHousehold(people.getHousehold());
            peopleVO.setAddress(people.getAddress());
            peopleVO.setSex(people.getSex());
            peopleVO.setAge(people.getAge());
            peopleVO.setBirthday(people.getBirthday());
            peopleVO.setPolitic(people.getPolitic());
            peopleVO.setEduLevel(people.getEdulevel());
            peopleVO.setJob(people.getJob());
            peopleVO.setBirthplace(people.getBirthplace());
            peopleVO.setCommunity(String.valueOf(people.getCommunity()));
            peopleVO.setImportant(people.getImportant());
            peopleVO.setCare(people.getCare());
            peopleVO.setLastTime(sdf.format(people.getLasttime()));
            peopleVO.setCreateTime(sdf.format(people.getCreatetime()));
            peopleVO.setUpdateTime(sdf.format(people.getUpdatetime()));
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
        }
        return peopleVO;
    }
}
