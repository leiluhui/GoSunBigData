package com.hzgc.service.people.param;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.people.model.People;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PeopleDTO implements Serializable {
    private String id;
    private String name;
    private String idCard;
    private Long region;
    private String household;
    private String address;
    private String sex;
    private Integer age;
    private String birthday;
    private String politic;
    private String eduLevel;
    private String job;
    private String birthplace;
    private List<Integer> flagId;
    private List<byte[]> idCardPic;
    private List<byte[]> capturePic;
    private List<String> imsi;
    private List<String> phone;
    private List<String> house;
    private List<String> car;

    public People peopleDTOShift_insert(PeopleDTO peopleDTO){
        People people = new People();
        people.setId(UuidUtil.getUuid().toUpperCase());
        people.setName(peopleDTO.name);
        people.setIdcard(peopleDTO.idCard);
        people.setRegion(peopleDTO.region);
        people.setHousehold(peopleDTO.household);
        people.setAddress(peopleDTO.address);
        people.setSex(peopleDTO.sex);
        people.setAge(peopleDTO.age);
        people.setBirthday(peopleDTO.birthday);
        people.setPolitic(peopleDTO.politic);
        people.setEdulevel(peopleDTO.eduLevel);
        people.setJob(peopleDTO.job);
        people.setBirthplace(peopleDTO.birthplace);
        return people;
    }

    public People peopleDTOShift_update(PeopleDTO peopleDTO) {
        People people = new People();
        people.setId(peopleDTO.id);
        people.setName(peopleDTO.name);
        people.setIdcard(peopleDTO.idCard);
        people.setRegion(peopleDTO.region);
        people.setHousehold(peopleDTO.household);
        people.setAddress(peopleDTO.address);
        people.setSex(peopleDTO.sex);
        people.setAge(peopleDTO.age);
        people.setBirthday(peopleDTO.birthday);
        people.setPolitic(peopleDTO.politic);
        people.setEdulevel(peopleDTO.eduLevel);
        people.setJob(peopleDTO.job);
        people.setBirthplace(peopleDTO.birthplace);
        return people;
    }
}
