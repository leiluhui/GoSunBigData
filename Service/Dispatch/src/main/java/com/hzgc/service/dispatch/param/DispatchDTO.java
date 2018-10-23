package com.hzgc.service.dispatch.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "添加，修改布控信息入参封装")
@Data
public class DispatchDTO implements Serializable {
    @ApiModelProperty(value = "布控ID")
    private String id;
    @ApiModelProperty(value = "布控区域ID")
    @NotNull
    private Long regionId;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "布控人脸")
    private String face;
    @ApiModelProperty(value = "阈值")
    private Float threshold;
    @ApiModelProperty(value = "布控车辆")
    private String car;
    @ApiModelProperty(value = "布控MAC")
    private String mac;
    @ApiModelProperty(value = "备注")
    private String notes;
}
