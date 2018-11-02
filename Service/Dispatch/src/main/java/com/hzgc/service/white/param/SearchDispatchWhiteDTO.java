package com.hzgc.service.white.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "信息展示入参封装")
@Data
public class SearchDispatchWhiteDTO implements Serializable {


    @ApiModelProperty(value = "查询内容")

    private String type;
    @ApiModelProperty(value = "查询区域")

    private Long region;
    @ApiModelProperty(value = "起始行数")

    private int start;
    @ApiModelProperty(value = "分页行数")

    private int limit;

    @ApiModelProperty(value = "相机组织")

    private String organization;
}
