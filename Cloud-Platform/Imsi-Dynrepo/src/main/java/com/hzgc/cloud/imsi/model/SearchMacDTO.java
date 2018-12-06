package com.hzgc.cloud.imsi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "MAC信息入参封装")
@Data
public class SearchMacDTO implements Serializable {
    @ApiModelProperty(value = "查询类型（0：ID，1：设备，2：mac）")
    @NotNull
    private int searchType;
    @ApiModelProperty(value = "查询内容")
    @NotNull
    private String searchVal;
    @ApiModelProperty(value = "小区ID")
    private String communityId;
    @ApiModelProperty(value = "起始行数")
    @NotNull
    private int start;
    @ApiModelProperty(value = "分页行数")
    @NotNull
    private int limit;
}
