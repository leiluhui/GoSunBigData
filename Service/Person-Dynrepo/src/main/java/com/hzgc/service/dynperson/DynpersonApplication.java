package com.hzgc.service.dynperson;

import com.hzgc.common.service.api.config.EnablePlatformService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnablePlatformService
@EnableHystrix
@EnableSwagger2
public class DynpersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynpersonApplication.class,args);
    }
}
