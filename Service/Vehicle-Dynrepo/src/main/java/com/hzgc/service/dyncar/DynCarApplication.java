package com.hzgc.service.dyncar;

import com.hzgc.common.service.api.config.EnableDeviceQueryService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableHystrix
@EnableSwagger2
@EnableDeviceQueryService
public class DynCarApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynCarApplication.class,args);
    }
}
