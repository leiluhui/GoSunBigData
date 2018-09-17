package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "后台返回对象封装")
@Data
public class PeopleVO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "区域ID")
    private String region;
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
    @ApiModelProperty(value = "小区")
    private String community;
    @ApiModelProperty(value = "重点人口")
    private Integer important;
    @ApiModelProperty(value = "关爱人口")
    private Integer care;
    @ApiModelProperty(value = "最后抓拍时间")
    private String lastTime;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
    @ApiModelProperty(value = "标签列表")
    private List<Integer> flag;
    @ApiModelProperty(value = "IMSI码列表")
    private List<String> imsi;
    @ApiModelProperty(value = "电话列表")
    private List<String> phone;
    @ApiModelProperty(value = "房产列表")
    private List<String> house;
    @ApiModelProperty(value = "车辆列表")
    private List<String> car;
    @ApiModelProperty(value = "照片（人口库查询页使用）")
    private byte[] picture;
    @ApiModelProperty(value = "证件照片列表（根据ID查询详细信息使用）")
    private List<byte[]> idcardPicture;
    @ApiModelProperty(value = "实采列表（根据ID查询详细信息使用）")
    private List<byte[]> capturePicture;
}
