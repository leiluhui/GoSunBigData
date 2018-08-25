package com.hzgc.service.clustering.config;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {
    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    @Bean
    public FtpRegisterClient ftpRegisterClient() {
        return new FtpRegisterClient(environment.getProperty("zk.address"));
    }
}
