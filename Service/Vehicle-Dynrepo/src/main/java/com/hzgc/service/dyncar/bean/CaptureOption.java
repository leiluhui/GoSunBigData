package com.hzgc.service.dyncar.bean;

import com.hzgc.common.service.api.bean.DeviceDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 抓拍查询入参
 */
@Data
public class CaptureOption implements Serializable {
    // 设备筛选
    private List<Long> deviceIds;
    // 开始日期,格式：xxxx-xx-xx xx:xx:xx
    private String startTime;
    // 截止日期,格式：xxxx-xx-xx xx:xx:xx
    private String endTime;
    //搜索的设备IPC列表
    private List<String> deviceIpcs;
    // ipc community-mapping device id
    private Map<String, DeviceDTO> ipcMappingDevice;
    // 属性
    private List<VehicleAttribute> attributes;
    // 车牌
    private String plate_licence;
    // 车标
    private String brand_name;
    // 排序参数
    private List<Integer> sort;
    // 分页查询开始行
    private int start;
    // 查询条数
    private int limit;

}
