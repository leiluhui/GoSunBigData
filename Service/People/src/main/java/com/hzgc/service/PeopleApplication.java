package com.hzgc.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = {"com.hzgc.service.people.dao","com.hzgc.service.community.dao"})
public class PeopleApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate (){
        return new RestTemplate();
    }
}

