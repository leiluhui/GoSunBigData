package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="小区迁出人口最后抓拍查询出参")
@Data
public class OutPeopleLastCaptureVO implements Serializable {
    @ApiModelProperty(value = "最后抓拍设备ID")
    private String deviceid;
    @ApiModelProperty(value = "最后抓拍时间")
    private String lastTime;
    @ApiModelProperty(value = "离线天数")
    private int lastDay;
}
