package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "实有,重点,关爱,新增,迁出人口查询入参封装类")
@Data
public class CommunityPeopleDTO implements Serializable {
    @ApiModelProperty(value = "小区ID")
    private Long communityId;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
