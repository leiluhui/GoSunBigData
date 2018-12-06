package com.hzgc.cloud.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "重点人员识别记录查询入参")
@Data
public class ImportantRecognizeDTO implements Serializable {
    @ApiModelProperty(value = "查询类型(0:抓拍（人脸抓拍）,1:识别（车辆识别、IMSI识别）,2:全查（人脸抓拍、车辆识别、IMSI识别)")
    private int searchType;
    @ApiModelProperty(value = "查询区域ID")
    private Long regionId;
    @ApiModelProperty(value = "起始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "起始行数")
    private int start;
    @ApiModelProperty(value = "分页行数")
    private int limit;
}
