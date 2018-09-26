package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "聚焦人员抓拍前端入参封装类")
@Data
public class PeopleCaptureDTO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String peopleId;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
