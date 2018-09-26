package com.hzgc.cluster.peoman.client.service;

import com.hzgc.cluster.peoman.client.zookeeper.Constant;
import com.hzgc.cluster.peoman.client.zookeeper.JobDiscover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class StartJobClient {
    @Autowired
    private JobDiscover jobDiscover;
    @Autowired
    private JobClientCallBack jobClientCallBack;

    //开始计算worker的个数并从TIDB中读取t_picture的个数，计算出每个节点需要启动多少个数据
    @PostConstruct
    public void catchNodeNumAndStartJob(){
        try {
            jobDiscover.startListen(jobClientCallBack, Constant.tempPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
