package com.hzgc.service.dispatch.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "布控告警历史记录返回封装")
@Data
public class DispatchRecognizeVO implements Serializable {
    @ApiModelProperty(value = "布控识别ID")
    @NotNull
    private String id;
    @ApiModelProperty(value = "布控ID")
    @NotNull
    private String dispatchId;
    @ApiModelProperty(value = "识别时间")
    @NotNull
    private String recordTime;
    @ApiModelProperty(value = "设备ID")
    @NotNull
    private String deviceId;
    @ApiModelProperty(value = "识别类型")
    private Integer type;
    @ApiModelProperty(value = "设备姓名")
    private String deviceName;
    @ApiModelProperty(value = "识别大图（人脸大图,车辆大图）")
    private String burl;
    @ApiModelProperty(value = "识别小图（人脸小图,车辆小图）")
    private String surl;
    @ApiModelProperty(value = "相似度(人脸布控)")
    private Float similarity;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "布控车辆")
    private String car;
    @ApiModelProperty(value = "布控MAC")
    private String mac;
    @ApiModelProperty(value = "备注")
    private String notes;
}
