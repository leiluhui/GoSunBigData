package com.hzgc.datasyncer.service;

import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
class CameraCacheHelper implements CacheHelper<String, CameraQueryDTO> {
    @Autowired
    @SuppressWarnings("unused")
    private RedisTemplate<String, CameraQueryDTO> redisTemplate;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    private String prefix = "camera_";

    @Override
    public void cacheInfo(String ipcId, CameraQueryDTO cameraInfo) {
        if (ipcId != null && ipcId.length() > 0 && cameraInfo != null) {
            redisTemplate.opsForValue().set(generateKey(this.prefix, ipcId), cameraInfo, 3600, TimeUnit.SECONDS);
            log.info("Cache camera info successfull, ipc id is:{}, camera info is:{}",
                    ipcId, JacksonUtil.toJson(cameraInfo));
        } else {
            log.error("Cache camera info failed, ipc id is:{}, camera info is:{}",
                    ipcId, JacksonUtil.toJson(cameraInfo));
        }
    }

    @Override
    public CameraQueryDTO getInfo(String ipcId) {
        if (ipcId != null && ipcId.length() > 0) {
            CameraQueryDTO cameraInfo = redisTemplate.opsForValue().get(generateKey(this.prefix, ipcId));
            if (cameraInfo != null) {
                return cameraInfo;
            } else {
                Map<String, CameraQueryDTO> cameraInfoMap =
                        platformService.getCameraInfoByBatchIpc(Collections.singletonList(ipcId));
                cameraInfo = cameraInfoMap.get(ipcId);
                if (cameraInfo != null) {
                    cacheInfo(ipcId, cameraInfo);
                    return cameraInfo;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String generateKey(String prefix, String ipcId) {
        return prefix + ipcId;
    }


    @Deprecated
    Map<String, CameraQueryDTO> getCameraInfo(List<String> ipcList) {
        if (ipcList != null && ipcList.size() > 0) {
            List<CameraQueryDTO> cameraInfo = redisTemplate.opsForValue().multiGet(ipcList);
            if (cameraInfo != null) {
                List<CameraQueryDTO> filterCameraInfo = cameraInfo.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (filterCameraInfo.size() > 0) {
                    Map<String, CameraQueryDTO> cameraInfoMap = filterCameraInfo.stream()
                            .collect(Collectors.toMap(CameraQueryDTO::getCameraCode, camera -> camera));
                    ipcList.removeAll(cameraInfoMap.keySet());
                    if (ipcList.isEmpty()) {
                        return cameraInfoMap;
                    } else {
                        Map<String, CameraQueryDTO> cameraInfoByQuery = platformService.getCameraInfoByBatchIpc(ipcList);
                        redisTemplate.opsForValue().multiSet(cameraInfoByQuery);
                        cameraInfoMap.putAll(cameraInfoByQuery);
                        return cameraInfoMap;
                    }
                } else {
                    Map<String, CameraQueryDTO> cameraInfoByQuery = platformService.getCameraInfoByBatchIpc(ipcList);
                    redisTemplate.opsForValue().multiSet(cameraInfoByQuery);
                    return cameraInfoByQuery;
                }
            }
        }
        return null;
    }
}
