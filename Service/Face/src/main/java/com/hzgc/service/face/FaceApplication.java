package com.hzgc.service.face;

import com.hzgc.common.faceattribute.service.AttributeService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import com.hzgc.service.face.controller.FaceController;
import com.hzgc.service.face.service.FaceExtractService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@PropertySource("application.properties")
@EnableSwagger2
@EnableAuthSynchronize
public class FaceApplication {

    public static void main(String[] args){
        SpringApplication.run(FaceApplication.class,args);
    }


    @Bean
    public AttributeService attributeService(){
        return  new AttributeService();
    }

}
