package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CaptureObject {
    @ApiModelProperty(value = "设备ID")
    private String ipcId;
    @ApiModelProperty(value = "时间")
    private String timeStamp;
    @ApiModelProperty(value = "日期")
    private String date;
    @ApiModelProperty(value = "时间段")
    private int timeSlot;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "小图地址")
    private String surl;
    @ApiModelProperty(value = "大图地址")
    private String burl;
    @ApiModelProperty(value = "去掉hostname的地址")
    private String relativePath;
    @ApiModelProperty(value = "hostname")
    private String hostname;
}
