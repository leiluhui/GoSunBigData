package com.hzgc.service.clustering;

import com.hzgc.common.service.api.config.EnableDeviceQueryService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@SpringBootApplication
@EnableAuthSynchronize
@EnableScheduling
@EnableDeviceQueryService
public class ClusteringApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClusteringApplication.class, args);
    }
}