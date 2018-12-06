package com.hzgc.cloud.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "小区迁入迁出人口统计（实有人口首页）出参")
@Data
public class NewAndOutPeopleCounVO implements Serializable {
    @ApiModelProperty(value = "小区Id")
    private Long communityId;
    @ApiModelProperty(value = "小区名字")
    private String communityName;
    @ApiModelProperty(value = "查询月份")
    private String month;
    @ApiModelProperty(value = "本月小区迁入总数量")
    private int SuggestNewCount;
    @ApiModelProperty(value = "本月小区迁出总数量")
    private int SuggestOutCount;
    @ApiModelProperty(value = "本月小区确认迁入总数量")
    private int confirmNewCount;
    @ApiModelProperty(value = "本月小区确认迁出总数量")
    private int confirmOutCount;
}
