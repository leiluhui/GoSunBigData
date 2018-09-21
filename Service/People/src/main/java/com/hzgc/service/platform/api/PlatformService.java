package com.hzgc.service.platform.api;

import com.hzgc.service.platform.param.Community;
import com.hzgc.service.platform.param.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PlatformService {

    @Autowired
    private RestTemplate restTemplate;

    public String getRegionName(Long regionId) {
        List<Long> regionIds = new ArrayList<>();
        regionIds.add(regionId);
        ResponseEntity<Region> responseEntity = restTemplate.postForEntity("http://region/query_region_info_by_ids", regionIds, Region.class);
        Region region = responseEntity.getBody();
        if (region == null || StringUtils.isBlank(region.getName())) {
            log.info("Get region name failed, because result is null");
            return null;
        }
        return region.getName();
    }

    public String getCommunityName(Long communityId) {
        List<Long> communityIds = new ArrayList<>();
        communityIds.add(communityId);
        ResponseEntity<Community> responseEntity = restTemplate.postForEntity("http://region/query_region_info_by_ids", communityIds, Community.class);
        Community community = responseEntity.getBody();
        if (community == null || StringUtils.isBlank(community.getCameraName())) {
            log.info("Get region name failed, because result is null");
            return null;
        }
        return community.getCameraName();
    }
}
