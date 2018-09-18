package com.hzgc.service.facedispatch;

import com.hzgc.common.service.api.service.DeviceQueryService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.hzgc.service.facedispatch.database.dao")
public class FaceDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(FaceDispatchApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    DeviceQueryService queryService() {
        return new DeviceQueryService();
    }

}
