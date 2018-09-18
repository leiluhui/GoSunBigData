package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "实有,重点,关爱,新增,迁出人口查询出参封装类")
@Data
public class CommunityPeopleVO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "最后抓拍时间")
    private String lastTime;
}
