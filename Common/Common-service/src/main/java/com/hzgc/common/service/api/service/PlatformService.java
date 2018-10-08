package com.hzgc.common.service.api.service;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.bean.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class PlatformService {
    @Resource(name = "platform")
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    /**
     * 获取区域/社区名称
     * @param id 区域/社区 ID
     * @return 区域/社区 名称
     */
    public String getMergerName(Long id) {
        if (id != null){
            log.info("Method:getMergerName, id:" + id);
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            List<Region> regions = getRegionByIds(ids);
            if (regions != null && regions.size() > 0 && regions.get(0) != null) {
                return regions.get(0).getMergerName();
            }else {
                log.info("Get region info failed, because result is null");
            }
        }else {
            log.error("Method:getMergerName, id is null");
        }
        return null;
    }

    public String getDeviceName(String deviceId){
        if (deviceId != null){
            log.info("Method:getDeviceName, deviceId:" + deviceId);
            List<String> ids = new ArrayList<>();
            ids.add(deviceId);
            Map<String, CameraQueryDTO> map = getCameraInfoByBatchIpc(ids);
            if (map != null && map.size() > 0 && map.get(deviceId) != null) {
                return map.get(deviceId).getCameraName();
            }else {
                log.info("Get device info failed, because result is null");
            }
        }else {
            log.error("Method:getDeviceName, id is null");
        }
        return null;
    }

    /**
     * 获取区域/社区信息
     * @param ids 区域/社区 ID列表
     * @return 获取区域/社区信息
     */
    private List<Region> getRegionByIds(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            log.info("Method:getRegionByIds, ids:" + JSON.toJSONString(ids));
            ParameterizedTypeReference<Region[]> parameterizedTypeReference =
                    new ParameterizedTypeReference<Region[]>() {};
            ResponseEntity<Region[]> responseEntity = restTemplate.exchange("http://172.18.18.40:8888/api/v1/region/internal/region/query_region_info_by_ids",
                    HttpMethod.POST, new HttpEntity<>(ids), parameterizedTypeReference);
            return Arrays.asList(responseEntity.getBody());
        }else {
            log.error("Method:getRegionByIds, ids is null");
            return new ArrayList<>();
        }
    }

    public Map<String, CameraQueryDTO> getCameraInfoByBatchIpc(List<String> ipcList) {
        if (ipcList != null) {
            log.info("Method:getCameraInfoByBatchIpc, ipc list is:" + Arrays.toString(ipcList.toArray()));
            ParameterizedTypeReference<Map<String, CameraQueryDTO>> parameterizedTypeReference =
                    new ParameterizedTypeReference<Map<String, CameraQueryDTO>>() {
                    };
            ResponseEntity<Map<String, CameraQueryDTO>> responseEntity =
                    restTemplate.exchange("http://172.18.18.40:8888/api/v1/device/internal/cameras/query_camera_by_codes", HttpMethod.POST,
                            new HttpEntity<>(ipcList), parameterizedTypeReference);
            return responseEntity.getBody();
        } else {
            log.error("Method:getCameraInfoByBatchIpc, ipc list is null");
            return new HashMap<>();
        }
    }
}