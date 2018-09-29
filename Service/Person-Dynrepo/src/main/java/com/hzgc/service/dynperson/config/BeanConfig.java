package com.hzgc.service.dynperson.config;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {
    @Autowired
    private Environment environment;

    @Bean
    public FtpRegisterClient register() {
        return new FtpRegisterClient(environment.getProperty("zk.address"));
    }

}

