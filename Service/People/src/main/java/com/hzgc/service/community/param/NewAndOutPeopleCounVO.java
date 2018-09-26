package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value ="小区疑似迁入迁出人口统计出参")
@Data
public class NewAndOutPeopleCounVO implements Serializable {
    @ApiModelProperty(value = "小区名字")
    private String communityName;
    @ApiModelProperty(value = "本月小区疑似迁入总数量")
    private int SuggestNewCount;
    @ApiModelProperty(value = "本月小区疑似迁出总数量")
    private int SuggestOutCount;
    @ApiModelProperty(value = "本月小区确认迁入总数量")
    private int confirmNewCount;
    @ApiModelProperty(value = "本月小区确认迁出总数量")
    private int confirmOutCount;
}
