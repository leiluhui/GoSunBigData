package com.hzgc.cloud.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "小区迁入迁出人口统计（实有人口首页）入参")
@Data
public class NewAndOutPeopleCountDTO implements Serializable {
    @ApiModelProperty(value = "查询小区ID列表")
    private List<Long> communityIdList;
    @ApiModelProperty(value = "查询月份")
    private String month;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
