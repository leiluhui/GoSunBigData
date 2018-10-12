package com.hzgc.service.people.param;

import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.service.people.model.People;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "前端入参封装类")
@Data
public class PeopleDTO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "区域ID")
    private Long region;
    @ApiModelProperty(value = "户籍")
    private String household;
    @ApiModelProperty(value = "现住地")
    private String address;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "生日")
    private String birthday;
    @ApiModelProperty(value = "政治面貌")
    private String politic;
    @ApiModelProperty(value = "文化程度")
    private String eduLevel;
    @ApiModelProperty(value = "职业")
    private String job;
    @ApiModelProperty(value = "籍贯")
    private String birthplace;
    @ApiModelProperty(value = "标签列表")
    private List<Integer> flagId;
    @ApiModelProperty(value = "证件照片列表")
    private List<String> idCardPic;
    @ApiModelProperty(value = "实采照片列表")
    private List<String> capturePic;
    @ApiModelProperty(value = "IMSI码列表")
    private List<String> imsi;
    @ApiModelProperty(value = "电话列表")
    private List<String> phone;
    @ApiModelProperty(value = "房产列表")
    private List<String> house;
    @ApiModelProperty(value = "车辆列表")
    private List<String> car;

    public People peopleDTOShift_insert(PeopleDTO peopleDTO) {
        People people = new People();
        if (StringUtils.isBlank(peopleDTO.id)) {
            people.setId(UuidUtil.getUuid());
        }else {
            people.setId(peopleDTO.id);
        }
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
        people.setUpdatetime(new Date());
        return people;
    }
}
