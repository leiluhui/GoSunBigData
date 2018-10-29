package com.hzgc.cluster.peoman.worker.service;

import com.hzgc.cluster.peoman.worker.zookeeper.WorkerRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JobRunnerImpl implements ApplicationRunner {
    @Autowired
    @SuppressWarnings("unused")
    private InnerConsumer innerConsumer;

    @Autowired
    @SuppressWarnings("unused")
    private FaceConsumer faceConsumer;

    @Autowired
    @SuppressWarnings("unused")
    private LoadDataFromTiDB loadDataFromTiDB;

    @Value("${zookeeper.address}")
    @SuppressWarnings("unused")
    private String zkAddress;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        WorkerRegister workerRegister = new WorkerRegister(zkAddress);
        List<String> nodes = workerRegister.getParenNode();
        log.info("========================Start run worker, zkAddress={}, nodes={}", zkAddress, StringUtils.join(nodes, ","));
        int offset = 0;
        int limit = 3000000;
        String workId = "0";
        if (nodes != null && nodes.size() > 0) {
            String lastNode = nodes.get(nodes.size() -1);
            if (lastNode.equals(String.valueOf(nodes.size() -1))) {
                offset = nodes.size() * limit;
                workId = String.valueOf(nodes.size());
            } else {
                for (int i=0; i<nodes.size(); i++) {
                    if (! nodes.contains(String.valueOf(i))) {
                        offset = i * limit;
                        workId = String.valueOf(i);
                        break;
                    }
                }
            }
        }
        workerRegister.regist(workId, "["+offset+","+(offset+limit)+")");
        log.info("========================Start run worker, worker id is {} ", workId);
        loadDataFromTiDB.load(offset, limit);
        innerConsumer.initInnerConsumer();
        Thread thread = new Thread(innerConsumer);
        thread.start();
        faceConsumer.initFaceConsumer(workId);
        new Thread(faceConsumer).start();
    }
}
