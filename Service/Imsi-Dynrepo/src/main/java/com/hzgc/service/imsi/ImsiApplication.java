package com.hzgc.service.imsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@EnableSwagger2
@SpringBootApplication
public class ImsiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsiApplication.class, args);
    }

}
