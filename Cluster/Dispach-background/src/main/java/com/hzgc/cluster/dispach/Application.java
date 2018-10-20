package com.hzgc.cluster.dispach;

import com.hzgc.common.service.api.config.EnablePlatformService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
@EnablePlatformService
@EnableAutoConfiguration
@EnableEurekaClient
@EnableKafka
@MapperScan("com.hzgc.cluster.peoman.worker.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
