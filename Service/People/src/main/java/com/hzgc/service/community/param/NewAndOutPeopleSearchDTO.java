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
    @ApiModelProperty(value = "查询类别（0:迁入,1:迁出）")
    private int type;
    @ApiModelProperty(value = "查询类别状态（0:不限,1:已确认,2:未确认）")
    private int typeStatus;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
