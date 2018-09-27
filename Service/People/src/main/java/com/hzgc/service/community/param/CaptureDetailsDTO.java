package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="小区迁入人口抓拍详情入参")
@Data
public class CaptureDetailsDTO implements Serializable {
    @ApiModelProperty(value = "迁入人口ID")
    private Long peopleId;
    @ApiModelProperty(value = "查询小区ID")
    private Long communityId;
    @ApiModelProperty(value = "查询月份")
    private String month;
}
