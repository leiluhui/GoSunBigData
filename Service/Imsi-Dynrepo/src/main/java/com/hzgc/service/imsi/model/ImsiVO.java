package com.hzgc.service.imsi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "IMSI信息出参封装")
@Data
public class ImsiVO implements Serializable {
    @ApiModelProperty(value = "手机IMSI码")
    private String imsi;
    @ApiModelProperty(value = "小区ID")
    private String cellid;
    @ApiModelProperty(value = "区域码")
    private String lac;
    @ApiModelProperty(value = "设备ID")
    private String controlsn;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "小区名称")
    private String communityName;
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    @ApiModelProperty(value = "识别时间")
    private String time;
}
