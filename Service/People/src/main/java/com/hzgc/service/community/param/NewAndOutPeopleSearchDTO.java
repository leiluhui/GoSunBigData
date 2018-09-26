package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="小区迁入迁出人口查询（疑似与确认）入参")
@Data
public class NewAndOutPeopleSearchDTO implements Serializable {
    @ApiModelProperty(value = "查询小区ID")
    private Long community;
    @ApiModelProperty(value = "查询月份")
    private String month;
    @ApiModelProperty(value = "查询类别（迁入或前出）")
    private int type;
    @ApiModelProperty(value = "类别状态（确认或未确认）")
    private int typeStatus;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
