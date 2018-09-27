package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="24小时抓拍统计")
@Data
public class CaptureHourCount implements Serializable {
    @ApiModelProperty(value = "时间点（小时）")
    private String hour;
    @ApiModelProperty(value = "抓拍统计")
    private int count;
}
