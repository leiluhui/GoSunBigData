package com.hzgc.cluster.peoman.client.service;

import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobClientReferenceBean implements InitializingBean {
    @Autowired
    private JobClient jobClient;

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
