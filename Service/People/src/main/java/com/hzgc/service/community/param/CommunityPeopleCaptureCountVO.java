package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "聚焦人员抓拍次数返回封装类")
@Data
public class CommunityPeopleCaptureCountVO implements Serializable {
    @ApiModelProperty(value = "抓拍设备")
    private String deviceId;
    @ApiModelProperty(value = "抓拍日期")
    private String date;
    @ApiModelProperty(value = "抓拍次数")
    private int count;
}
