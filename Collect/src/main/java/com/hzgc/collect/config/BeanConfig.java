package com.hzgc.collect.config;

import com.hzgc.common.service.carattribute.service.CarAttributeService;
import com.hzgc.common.service.faceattribute.service.AttributeService;
import com.hzgc.common.service.personattribute.service.PersonAttributeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
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
