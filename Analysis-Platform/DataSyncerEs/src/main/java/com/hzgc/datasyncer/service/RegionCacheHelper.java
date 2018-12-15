package com.hzgc.datasyncer.service;

import com.hzgc.common.service.api.bean.Region;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RegionCacheHelper implements CacheHelper<Long, Region> {
    @Autowired
    private RedisTemplate<String, Region> redisTemplate;

    @Autowired
    private PlatformService platformService;

    private String prefix = "region_";

    @Override
    public String generateKey(String prefix, Long key) {
        return prefix + key;
    }

    @Override
    public void cacheInfo(Long regionId, Region region) {
        if (regionId != null && region != null) {
            redisTemplate.opsForValue().set(generateKey(prefix, regionId), region, 3600, TimeUnit.SECONDS);
            log.info("Cache region info successfull, region id is:{}, region info is:{}",
                    regionId, JacksonUtil.toJson(region));
        } else {
            log.error("Cache region info failed, region id is:{}, region info is:{}",
                    regionId, JacksonUtil.toJson(region));
        }
    }

    @Override
    public Region getInfo(Long regionId) {
        if (regionId != null) {
            Region region = redisTemplate.opsForValue().get(generateKey(this.prefix, regionId));
            if (region != null) {
                return region;
            } else {
                List<Region> regionByIds = platformService.getRegionByIds(Collections.singletonList(regionId));
                if (regionByIds != null && regionByIds.get(0) != null) {
                    cacheInfo(regionId, regionByIds.get(0));
                    return regionByIds.get(0);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
