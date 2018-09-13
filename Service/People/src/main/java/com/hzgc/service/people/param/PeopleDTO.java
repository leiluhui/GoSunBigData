package com.hzgc.service.people.param;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.service.PeopleService;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端入参
 */
@ApiModel(value = "前端入参封装类")
@Data
public class PeopleDTO implements Serializable {
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

    private List<Integer> flag;

    private List<byte[]> idCardPic;

    private List<byte[]> capturePic;

    private List<String> imsi;

    private List<String> phone;

    private List<String> house;

    private List<String> car;

    public People peopleDTOShift(PeopleDTO peopleDTO, String str){
        People people = new People();
        if (PeopleService.INSERT.equals(str)){
            people.setId(UuidUtil.getUuid().toUpperCase());
        }
        if (PeopleService.UPDATE.equals(str)){
            people.setId(peopleDTO.id);
        }
        people.setName(peopleDTO.name);
        people.setIdcard(peopleDTO.idcard);
        people.setRegion(peopleDTO.region);
        people.setHousehold(peopleDTO.household);
        people.setAddress(peopleDTO.address);
        people.setSex(peopleDTO.sex);
        people.setAge(peopleDTO.age);
        people.setBirthday(peopleDTO.birthday);
        people.setPolitic(peopleDTO.politic);
        people.setEdulevel(peopleDTO.edulevel);
        people.setJob(peopleDTO.job);
        people.setBirthplace(peopleDTO.birthplace);
        return people;
    }
}
