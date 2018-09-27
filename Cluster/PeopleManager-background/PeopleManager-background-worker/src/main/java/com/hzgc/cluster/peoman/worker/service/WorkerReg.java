package com.hzgc.cluster.peoman.worker.service;

import com.hzgc.cluster.peoman.worker.zookeeper.WorkerRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkerReg {

    public WorkerReg(@Value("${zookeeper.address}") String zkAddress,@Value("${lts.tasktracker.node-group}")String workerId) {
        log.info("zkAddress is : " + zkAddress);
        WorkerRegister workerRegister = new WorkerRegister(zkAddress);
        try {
            workerRegister.regist(workerId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
