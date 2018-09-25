package com.hzgc.service.facedispatch;

import com.hzgc.common.service.api.config.EnablePlatformService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnablePlatformService
@MapperScan(value = "com.hzgc.service.facedispatch.starepo.dao")
public class FaceDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(FaceDispatchApplication.class, args);
    }
}
