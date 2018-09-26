package com.hzgc.cluster.peoman.worker;

import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import com.hzgc.common.service.api.config.EnablePlatformService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTaskTracker
@EnablePlatformService
@EnableAutoConfiguration
@MapperScan("com.hzgc.cluster.peoman.worker.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
