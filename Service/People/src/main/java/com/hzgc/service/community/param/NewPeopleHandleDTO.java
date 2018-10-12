package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "小区迁入新增人员操作入参")
@Data
public class NewPeopleHandleDTO implements Serializable {
    @ApiModelProperty(value = "新增人员ID")
    private String newPeopleId;
    @ApiModelProperty(value = "查询人员ID")
    private String searchPeopleId;
    @ApiModelProperty(value = "社区ID")
    private Long communityId;
    @ApiModelProperty(value = "新增人员原图")
    private String capturePicture;
}
