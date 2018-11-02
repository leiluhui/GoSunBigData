package com.hzgc.service.white.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "添加，修改白名单布控信息入参封装")
@Data
public class DispatchWhiteDTO implements Serializable {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "人员信息")
    private List<WhiteName> name_list;

    @ApiModelProperty(value = "名称")
    private String designation;

    @ApiModelProperty(value = "设备ids")
    private List<DeviceIps> ipc_list;

    @ApiModelProperty(value = "相机组织")
    private String organization;
}

