package com.hzgc.service.dynperson.bean;

import com.hzgc.common.service.personattribute.bean.PersonAttribute;
import com.hzgc.common.service.api.bean.DeviceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel(value = "筛选条件")
@Data
@Slf4j
@ToString
public class CaptureOption implements Serializable {


    //搜索的deviceId列表
    @ApiModelProperty(value ="搜索的deviceId列表")
    private List<Long> deviceIds;

    //截止日期,格式：xxxx-xx-xx xx:xx:xx
    @ApiModelProperty(value ="截止日期")
    private String endTime;

    //开始日期,格式：xxxx-xx-xx xx:xx:xx
    @ApiModelProperty(value ="开始日期")
    private String startTime;

    //排序参数
    @ApiModelProperty(value ="排序参数")
    private List<Integer> sort;

    //分页查询开始行
    @ApiModelProperty(value ="分页查询开始行")
    private int start;

    //查询条数
    @ApiModelProperty(value ="查询条数")
    private int limit;

    //参数筛选选项
    @ApiModelProperty(value ="参数筛选选项")
    private List<PersonAttribute> attributes;

    //搜索的设备IPC列表
    @ApiModelProperty(value ="搜索的设备IPC列表")
    private List<String> deviceIpcs;

    // ipc mapping device id
    private Map<String, DeviceDTO> ipcMappingDevice;


}
