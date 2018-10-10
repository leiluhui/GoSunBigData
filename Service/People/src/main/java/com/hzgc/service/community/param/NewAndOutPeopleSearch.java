package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "小区迁入迁出人口查询（疑似与确认）返回对象封装")
@Data
public class NewAndOutPeopleSearch implements Serializable {
    @ApiModelProperty(value = "人员全局ID")
    private String peopleId;
    @ApiModelProperty(value = "小区Id")
    private Long communityId;
    @ApiModelProperty(value = "查询月份")
    private String month;
    @ApiModelProperty(value = "照片ID")
    private Long picture;
    @ApiModelProperty(value = "照片地址")
    private String sul;
    @ApiModelProperty(value = "类别（0:迁入,1:迁出）")
    private int type;
    @ApiModelProperty(value = "标签（0:已确认迁入,1:已确认未迁入,2:未确认迁入,3:已确认迁出,4:已确认未迁出,5:未确认迁出）")
    private int isconfirm;
    @ApiModelProperty(value = "迁入标签（0:预实名,1:新增）")
    private int flag;
}
