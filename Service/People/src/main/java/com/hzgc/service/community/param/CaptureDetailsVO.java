package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value ="小区迁入人口抓拍详情出参")
@Data
public class CaptureDetailsVO implements Serializable {
    @ApiModelProperty(value = "各相机点位抓拍统计")
    private List<CaptureDeviceCount> deviceCounts;
    @ApiModelProperty(value = "24小时抓拍统计")
    private List<CaptureHourCount> hourCounts;
    @ApiModelProperty(value = "迁入人员抓拍统计（含总条数）")
    private CapturePeopleCount peopleCount;
}
