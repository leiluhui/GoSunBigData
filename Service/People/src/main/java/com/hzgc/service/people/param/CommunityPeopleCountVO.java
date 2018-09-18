package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "实有,重点,关爱,新增,迁出人口数量统计封装类")
@Data
public class CommunityPeopleCountVO implements Serializable {
    @ApiModelProperty(value = "实有人员数量")
    private int communityPeoples;
    @ApiModelProperty(value = "重点人员数量")
    private int importantPeoples;
    @ApiModelProperty(value = "关爱人员数量")
    private int carePeoples;
    @ApiModelProperty(value = "新增人员数量")
    private int newPeoples;
    @ApiModelProperty(value = "迁出人员数量")
    private int outPeoples;
}
