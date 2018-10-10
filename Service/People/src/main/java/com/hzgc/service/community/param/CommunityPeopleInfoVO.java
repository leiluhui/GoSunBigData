package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "小区迁入迁出人口信息出参")
@Data
public class CommunityPeopleInfoVO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "生日")
    private String birthday;
    @ApiModelProperty(value = "籍贯")
    private String birthplace;
    @ApiModelProperty(value = "现住地")
    private String address;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "照片ID")
    private Long pictureId;
}
