package com.hzgc.service.people.param;

import com.hzgc.service.people.model.People;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang.math.RandomUtils;

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

    public People peopleDTOShift(PeopleDTO peopleDTO){
        if (peopleDTO == null){
            return null;
        }
        People people = new People();
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
        people.setCommunity(peopleDTO.community);
        long randomLong = RandomUtils.nextLong();
        if (peopleDTO.getFlag()!= null && peopleDTO.getFlag().size() > 0){
            people.setFlag(randomLong);
        }
        if (peopleDTO.getIdCardPic()!= null && peopleDTO.getIdCardPic().size() > 0){
            people.setIdcardpic(randomLong);
        }
        if (peopleDTO.getCapturePic()!= null && peopleDTO.getCapturePic().size() > 0){
            people.setCapturepic(randomLong);
        }
        if (peopleDTO.getImsi()!= null && peopleDTO.getImsi().size() > 0){
            people.setImsi(randomLong);
        }
        if (peopleDTO.getPhone()!= null && peopleDTO.getPhone().size() > 0){
            people.setPhone(randomLong);
        }
        if (peopleDTO.getHouse()!= null && peopleDTO.getHouse().size() > 0){
            people.setHouse(randomLong);
        }
        if (peopleDTO.getCar()!= null && peopleDTO.getCar().size() > 0){
            people.setCar(randomLong);
        }
        Date createTime = new Date();
        people.setCreatetime(createTime);
        people.setUpdatetime(createTime);
        return people;
    }
}
