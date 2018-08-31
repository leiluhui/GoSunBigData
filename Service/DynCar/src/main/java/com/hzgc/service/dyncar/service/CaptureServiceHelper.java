package com.hzgc.service.dyncar.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.DeviceQueryService;
import com.hzgc.service.dyncar.bean.CaptureOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 动态库实现类
 */
@Component
@Slf4j
public class CaptureServiceHelper {

    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    @Autowired
    @SuppressWarnings("unused")
    private DeviceQueryService queryService;

    @Autowired
    @SuppressWarnings("unused")
    private FtpRegisterClient register;

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    String getFtpUrl(String ftpUrl, String ip) {
        return ftpUrl.replace(ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":")), ip);
    }

    /**
     * 向CaptureOption中添加转换后的IPC列表以及IPC映射DeviceDTO
     *
     * @param option 抓拍查询参数
     */
    public void capturOptionConver(CaptureOption option) {
        List<String> ipcList;
        Map<String, DeviceDTO> ipcListMapping;
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

