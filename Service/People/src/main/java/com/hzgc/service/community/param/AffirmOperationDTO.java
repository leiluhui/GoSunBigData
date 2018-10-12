package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "小区人口确认迁入,迁出操作")
@Data
public class AffirmOperationDTO implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String peopleId;
    @ApiModelProperty(value = "小区ID")
    private Long communityId;
    @ApiModelProperty(value = "月份")
    private String month;
    @ApiModelProperty(value = "确认标签:迁入/出（2:已确认迁入/出,3:已确认未迁入/出）")
    private int isconfirm;
    @ApiModelProperty(value = "迁入状态（0:预实名,1:新增）")
    private int flag;
}
