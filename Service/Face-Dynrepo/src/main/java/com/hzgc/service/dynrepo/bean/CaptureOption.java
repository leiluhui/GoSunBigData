package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.service.faceattribute.bean.Attribute;
import com.hzgc.common.service.api.bean.DeviceDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class CaptureOption implements Serializable {
    //搜索的设备IPC列表
    private List<Device> deviceIpcs;
    // ipc mapping device id
    private Map<String, Device> ipcMapping;
    //开始日期,格式：xxxx-xx-xx xx:xx:xx
    private String startTime;
    //截止日期,格式：xxxx-xx-xx xx:xx:xx
    private String endTime;
    //参数筛选选项
    private List<Attribute> attributes;
    //排序参数
    private List<Integer> sort;
    //分页查询开始行
    private int start;
    //查询条数
    private int limit;

}
