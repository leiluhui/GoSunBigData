package com.hzgc.common.service.api.config;

import com.hzgc.common.service.api.service.PlatformService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EnablePlatformServiceConfig {
    @ConditionalOnMissingBean(RestTemplate.class)
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(PlatformService.class)
    public PlatformService deviceQueryService() {
        return new PlatformService();
    }

}
