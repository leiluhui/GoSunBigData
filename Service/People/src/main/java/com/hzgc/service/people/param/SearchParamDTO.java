package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "人口库查询入参封装类")
@Data
public class SearchParamDTO implements Serializable {
    @ApiModelProperty(value = "查询类型")
    private Long searchType;                       // 0 :姓名 1：身份证 2 ： IMSI 3 : 手机号
    @ApiModelProperty(value = "用户输入内容")
    private String searchVal;
    @ApiModelProperty(value = "区域ID")
    private Long regionId;
    @ApiModelProperty(value = "小区ID")
    private Long communityId;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}

