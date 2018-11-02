package com.hzgc.service.white.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "信息展示返回封装")
@Data
public class DispatchWhiteVO implements Serializable {
    @ApiModelProperty(value = "名称ID")
    private String id;

    @ApiModelProperty(value = "布控名称")
    private String designation;

    @ApiModelProperty(value = "展示人员姓名")
    private String show_name;

    @ApiModelProperty(value = "展示设备名称")
    private String show_ipc;

    @ApiModelProperty(value = "人员信息列表")
    private List<WhiteName> name_list;

    @ApiModelProperty(value = "设备信息列表")
    private List<DeviceIps> ipc_list;

    @ApiModelProperty(value = "状态值")
    private int  status;

}

