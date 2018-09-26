package com.hzgc.common.service.api.service;

import com.hzgc.common.service.api.bean.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class PlatformService {
    @Autowired
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getDeviceInfoByIpcError")
    @SuppressWarnings("unused")
    public DeviceDTO getDeviceInfoByIpc(String ipcId) {
        if (!StringUtils.isBlank(ipcId)) {
            return restTemplate.getForObject("http://device/internal/devices/query_by_serial/" + ipcId, DeviceDTO.class);
        }
        return new DeviceDTO();
    }

    @SuppressWarnings("unused")
    public DeviceDTO getDeviceInfoByIpcError(String ipcId) {
        log.error("Get deivce info by ipc error, ipcId is:" + ipcId);
        return new DeviceDTO();
    }

    @HystrixCommand(fallbackMethod = "getDeviceInfoByBatchIpcError")
    @SuppressWarnings("all")
    public Map<String, DeviceDTO> getDeviceInfoByBatchIpc(List<String> ipcList) {
        if (ipcList != null && ipcList.size() > 0) {
            ParameterizedTypeReference<Map<String, DeviceDTO>>
                    parameterizedTypeReference = new ParameterizedTypeReference<Map<String, DeviceDTO>>() {
            };
            ResponseEntity<Map<String, DeviceDTO>> responseEntity =
                    restTemplate.exchange("http://device/internal/devices/batch_query_by_serial",
                            HttpMethod.POST,
                            new HttpEntity<>(ipcList),
                            parameterizedTypeReference);
            return responseEntity.getBody();
        }
        return new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Map<String, DeviceDTO> getDeviceInfoByBatchIpcError(List<String> ipcList) {
        log.error("Get device info by batch ipc error, ipcId list is:" + ipcList);
        return new HashMap<>();
    }

    @SuppressWarnings("unused")
    public List<Long> query_device_id(Long areaId, String level) {
        if (areaId != null) {
            log.info("Method:query_device_id, area id is:?, level is:?", areaId, level);
            ResponseEntity<Long[]> responseEntity = restTemplate.getForEntity(
                    "http://region/internal/region/query_device_id/" + areaId + "/level",
                    Long[].class
            );
            return Arrays.asList(responseEntity.getBody());
        } else {
            log.error("Method:query_device_id, area id is:?, level is:?", areaId, level);
            return new ArrayList<>();
        }

    }

    @HystrixCommand(fallbackMethod = "getDeviceInfoByIdError")
    @SuppressWarnings("unused")
    public DeviceDTO getDeviceInfoById(Long id) {
        if (id != null) {
            return restTemplate.getForObject("http://device/internal/devices/query_by_id/" + id, DeviceDTO.class);
        }
        return new DeviceDTO();
    }

    @SuppressWarnings("unused")
    public DeviceDTO getDeviceInfoByIdError(Long id) {
        log.error("Get device info by id error, id is:" + id);
        return new DeviceDTO();
    }

    @HystrixCommand(fallbackMethod = "getDeviceInfoByBatchIdError")
    public Map<String, DeviceDTO> getDeviceInfoByBatchId(List<String> idList) {
        if (idList != null && idList.size() > 0) {
            ParameterizedTypeReference<Map<String, DeviceDTO>>
                    parameterizedTypeReference = new ParameterizedTypeReference<Map<String, DeviceDTO>>() {
            };
            ResponseEntity<Map<String, DeviceDTO>> responseEntity =
                    restTemplate.exchange("http://device/internal/devices/batch_query_by_id",
                            HttpMethod.POST,
                            new HttpEntity<>(idList),
                            parameterizedTypeReference);
            return responseEntity.getBody() == null ? new HashMap<>() : responseEntity.getBody();
        }
        return new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Map<String, DeviceDTO> getDeviceInfoByBatchIdError(List<String> idList) {
        log.error("Get device info by batch id error, id List is:" + idList);
        return new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Map<Long, WebgisMapPointDTO> getDeviceInfoByBatchIdByDevice(List<Long> deviceIds) {
        if (deviceIds != null && deviceIds.size() > 0) {
            ParameterizedTypeReference<Map<Long, WebgisMapPointDTO>> parameterizedTypeReference = new ParameterizedTypeReference<Map<Long, WebgisMapPointDTO>>() {
            };
            ResponseEntity<Map<Long, WebgisMapPointDTO>> responseEntity = restTemplate.exchange("http://gis/internal/gis/batch_query_by_id",
                    HttpMethod.POST,
                    new HttpEntity<>(deviceIds),
                    parameterizedTypeReference);
            log.info("responseEntity's Body is :" + responseEntity.getBody());
            return responseEntity.getBody();
        }
        return new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Map<Long, RegionDTO> getRegionNameByRegionId(List<Long> regionIds) {
        if (regionIds != null && regionIds.size() > 0) {
            log.info("Method:getRegionNameByRegionId, region id list is:" + Arrays.toString(regionIds.toArray()));
            ParameterizedTypeReference<Map<Long, RegionDTO>> parameterizedTypeReference = new ParameterizedTypeReference<Map<Long, RegionDTO>>() {
            };
            ResponseEntity<Map<Long, RegionDTO>> responseEntity = restTemplate.exchange("http://region/internal/region/query_region_by_id",
                    HttpMethod.POST,
                    new HttpEntity<>(regionIds),
                    parameterizedTypeReference);
            log.info("responseEntity's Body is : " + responseEntity.getBody());
            return responseEntity.getBody();
        } else {
            log.error("Method:getRegionNameByRegionId, region id list is error");
            return new HashMap<>();
        }
    }

    public String getRegionName(Long regionId) {
        if (regionId != null) {
            log.info("Method:getRegionName, region id is:" + regionId);
            List<Long> regionIds = new ArrayList<>();
            regionIds.add(regionId);
            ResponseEntity<Region> responseEntity = restTemplate.postForEntity("http://region/query_region_info_by_ids", regionIds, Region.class);
            Region region = responseEntity.getBody();
            if (region == null || StringUtils.isBlank(region.getName())) {
                log.info("Get region name failed, because result is null");
                return null;
            }
            return region.getName();
        } else {
            log.error("Method:getRegionName, region id is:" + regionId);
            return null;
        }
    }

    public String getCommunityName(Long communityId) {
        if (communityId != null) {
            log.info("Method:getCommunityName, community id " + communityId);
            List<Long> communityIds = new ArrayList<>();
            communityIds.add(communityId);
            ResponseEntity<Community> responseEntity = restTemplate.postForEntity("http://region/query_region_info_by_ids", communityIds, Community.class);
            Community community = responseEntity.getBody();
            if (community == null || StringUtils.isBlank(community.getCameraName())) {
                log.info("Get region name failed, because result is null");
                return null;
            }
            return community.getCameraName();
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
                    restTemplate.exchange("http://device/internal/cameras/query_camera_by_codes", HttpMethod.POST,
                            new HttpEntity<>(ipcList), parameterizedTypeReference);
            return responseEntity.getBody();
        } else {
            log.error("Method:getCameraInfoByBatchIpc, ipc list is null");
            return new HashMap<>();
        }

    }
}