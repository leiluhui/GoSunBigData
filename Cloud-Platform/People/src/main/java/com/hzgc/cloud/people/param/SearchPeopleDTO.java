package com.hzgc.cloud.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "人口库模糊查询入参")
@Data
public class SearchPeopleDTO implements Serializable {
    @ApiModelProperty(value = "查询类型(0:姓名,1：身份证,2：IMSI,3:手机号,4:标签,5:IMEI)")
    private Long searchType;
    @ApiModelProperty(value = "用户输入内容")
    private String searchVal;
    @ApiModelProperty(value = "区域ID")
    private List<Long> regionIds;
    @ApiModelProperty(value = "小区ID")
    private Long communityId;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}

