package com.hzgc.cloud.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "迁入人员抓拍统计（含总条数）")
@Data
public class CapturePeopleCount implements Serializable {
    @ApiModelProperty(value = "总条数")
    private int total;
    @ApiModelProperty(value = "抓拍照片信息")
    private List<CapturePictureInfo> pictureInfos;
}
