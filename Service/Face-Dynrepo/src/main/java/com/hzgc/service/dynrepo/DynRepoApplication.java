package com.hzgc.service.dynrepo;

import com.hzgc.common.service.api.config.EnablePlatformService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableHystrix
@EnablePlatformService
public class DynRepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynRepoApplication.class, args);
    }
}
