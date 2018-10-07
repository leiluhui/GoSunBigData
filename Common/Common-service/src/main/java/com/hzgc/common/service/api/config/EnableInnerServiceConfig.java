package com.hzgc.common.service.api.config;

import com.hzgc.common.service.api.service.InnerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EnableInnerServiceConfig {

    @Bean(name = "inner")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(InnerService.class)
    public InnerService innerService() {
        return new InnerService();
    }
}
