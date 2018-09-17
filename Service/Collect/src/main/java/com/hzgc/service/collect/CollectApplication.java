package com.hzgc.service.collect;

import com.hzgc.common.service.carattribute.service.CarAttributeService;
import com.hzgc.common.service.faceattribute.service.AttributeService;
import com.hzgc.common.service.personattribute.service.PersonAttributeService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableAuthSynchronize
public class CollectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectApplication.class, args);
    }

    @Bean
    AttributeService attributeService() {
        return new AttributeService();
    }

    @Bean
    PersonAttributeService personAttributeService() {
        return new PersonAttributeService();
    }

    @Bean
    CarAttributeService carAttributeService() {
        return new CarAttributeService();
    }
}
