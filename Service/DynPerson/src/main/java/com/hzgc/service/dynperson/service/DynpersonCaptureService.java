package com.hzgc.service.dynperson.service;

import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.DeviceQueryService;
import com.hzgc.service.dynperson.bean.CaptureOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DynpersonCaptureService {

    @Autowired
    @SuppressWarnings("unused")
    private DeviceQueryService queryService;

    /**
     * 向CaptureOption中添加转换后的IPC列表以及IPC映射DeviceDTO
     *
     * @param option 抓拍查询参数
     */
    public void captureOptionConver(CaptureOption option) {
        List<String> ipcList;
        Map<String, DeviceDTO> ipcListMapping;

        log.info("device is : " + option.getDeviceIds());
        Map<String, DeviceDTO> result = queryService.getDeviceInfoByBatchId(option.getDeviceIds());
        if (!result.values().isEmpty() && result.values().size() > 0) {
            ipcListMapping = result.values().stream().collect(Collectors.toMap(DeviceDTO::getSerial, DeviceDTO -> DeviceDTO));
            ipcList = result.values().stream().map(DeviceDTO::getSerial).collect(toList());
        } else {
            log.error("Failed to find device id");
            ipcListMapping = new HashMap<>();
            ipcList = new ArrayList<>();
        }
        option.setDeviceIpcs(ipcList);
        option.setIpcMappingDevice(ipcListMapping);
    }
}
