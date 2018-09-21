package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value ="小区疑似迁入迁出人口统计入参")
@Data
public class SuggestPeopleDTO implements Serializable {
    @ApiModelProperty(value = "查询小区ID列表")
    private List<Long> communityIdList;         // 选填
    @ApiModelProperty(value = "查询月份")
    private String month;
    @ApiModelProperty(value = "查询区域ID")
    private Long regionId;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
