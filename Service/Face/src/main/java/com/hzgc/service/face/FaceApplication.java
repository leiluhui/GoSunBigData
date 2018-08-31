package com.hzgc.service.face;

import com.hzgc.common.faceattribute.service.AttributeService;
import com.hzgc.common.personattribute.service.PersonAttributeService;
import com.hzgc.common.service.api.config.EnableDeviceQueryService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import com.hzgc.service.face.service.PersonExtractService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableAuthSynchronize
@EnableDeviceQueryService
public class FaceApplication {

    public static void main(String[] args){
        SpringApplication.run(FaceApplication.class,args);
    }

    @Bean
    AttributeService attributeService(){
        return new AttributeService();
    }

    @Bean
    PersonAttributeService personAttributeService(){
        return new PersonAttributeService();
    }
}
