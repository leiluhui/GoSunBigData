package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="各相机点位抓拍统计")
@Data
public class CaptureDeviceCount implements Serializable {
    @ApiModelProperty(value = "相机ID")
    private String deviceName;
    @ApiModelProperty(value = "抓拍统计")
    private int count;
}

