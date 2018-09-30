package com.hzgc.common.service.api.service;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.bean.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PlatformService {
    @Autowired
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    public List<Region> getRegionByIds(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            log.info("Method:getRegionName, region ids is:" + JSON.toJSONString(ids));
            List<Region> list = restTemplate.postForObject("http://172.18.18.40:8888/api/v1/region/internal/region/query_region_info_by_ids", ids, List.class);
            if (list == null || list.size() <= 0) {
                log.info("Get region name failed, because result is null");
                return null;
            }
            return list;
        } else {
            log.error("Method:getRegionName, region ids is:" + JSON.toJSONString(ids));
            return null;
        }
    }

    public String getCommunityName(Long communityId) {
        if (communityId != null) {
            log.info("Method:getCommunityName, community id " + communityId);
            String name = restTemplate.getForObject("http://172.18.18.40:8888/api/v1/region/internal/region/query_region_name_by_id?id=" + communityId, String.class);
            if (name == null || name.length() <= 0) {
                log.info("Get region name failed, because result is null");
                return null;
            }
            return name;
        } else {
            log.error("Method:getCommunityName, community id is null");
            return null;
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