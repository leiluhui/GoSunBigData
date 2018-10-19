package com.hzgc.service.dispatch.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "布控信息展示入参封装")
@Data
public class SearchDispatchDTO implements Serializable {
    @ApiModelProperty(value = "查询类型（0：姓名，1：身份证，2：车牌，3：MAC）")
    @NotNull
    private String searchType;
    @ApiModelProperty(value = "查询内容")
    @NotNull
    private String searchVal;
    @ApiModelProperty(value = "查询区域")
    @NotNull
    private Long regionId;
    @ApiModelProperty(value = "起始行数")
    @NotNull
    private int start;
    @ApiModelProperty(value = "分页行数")
    @NotNull
    private int limit;
}
