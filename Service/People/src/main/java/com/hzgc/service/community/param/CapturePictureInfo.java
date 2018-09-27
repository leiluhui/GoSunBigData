package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="迁入人员抓拍统计（含总条数）")
@Data
public class CapturePictureInfo implements Serializable {
    @ApiModelProperty(value = "抓拍设备ID")
    private String deviceId;
    @ApiModelProperty(value = "抓拍设备地点")
    private String deviceName;
    @ApiModelProperty(value = "抓拍时间")
    private String captureTime;
    @ApiModelProperty(value = "抓拍照片")
    private String picture;     // 暂定String
}
