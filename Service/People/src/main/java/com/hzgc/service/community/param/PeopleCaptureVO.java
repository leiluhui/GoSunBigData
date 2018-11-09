package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "聚焦人员抓拍返回封装类")
@Data
public class PeopleCaptureVO implements Serializable {
    @ApiModelProperty(value = "抓拍时间")
    private String captureTime;
    @ApiModelProperty(value = "人脸、车辆抓拍设备")
    private String cameraDeviceId;
    @ApiModelProperty(value = "电围抓拍设备")
    private String imsiDeviceId;
    @ApiModelProperty(value = "人脸、车辆抓拍小图")
    private String surl;
    @ApiModelProperty(value = "人脸、车辆抓拍大图")
    private String burl;
    @ApiModelProperty(value = "识别车牌")
    private String plate;
    @ApiModelProperty(value = "识别IMSI码")
    private String imsi;
}
