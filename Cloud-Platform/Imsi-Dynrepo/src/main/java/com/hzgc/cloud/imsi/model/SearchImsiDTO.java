package com.hzgc.cloud.imsi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "IMSI信息入参封装")
@Data
public class SearchImsiDTO implements Serializable {
    @ApiModelProperty(value = "查询类型（0：设备ID，1：imsi）")
    @NotNull
    private int searchType;
    @ApiModelProperty(value = "查询内容")
    @NotNull
    private String searchVal;
    @ApiModelProperty(value = "小区ID")
    private String cellid;
    @ApiModelProperty(value = "区域码")
    private String lac;
    @ApiModelProperty(value = "起始行数")
    @NotNull
    private int start;
    @ApiModelProperty(value = "分页行数")
    @NotNull
    private int limit;
}
