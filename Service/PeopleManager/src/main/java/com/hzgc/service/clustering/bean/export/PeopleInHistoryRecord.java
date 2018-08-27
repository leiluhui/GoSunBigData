package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(value = "单个历史记录出参")
@Data
@ToString
public class PeopleInHistoryRecord {
    @ApiModelProperty(value = "设备名称")
    private String ipcName;

    @ApiModelProperty(value = "抓拍记录时间")
    private String recordTime;

    @ApiModelProperty(value = "抓拍小图url")
    private String surl;

    @ApiModelProperty(value = "抓拍大图url")
    private String burl;
}
