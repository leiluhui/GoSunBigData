package com.hzgc.compare.worker;

import com.hzgc.compare.worker.conf.Config;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperRegistry implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    private CuratorFramework zkClient;
    private String data = "";

    ZookeeperRegistry(String workerId, String nodeGroup, String port, String taskId){
        zkClient = connect();
        data = workerId + "," + nodeGroup + "," + port + "," + taskId;
    }

    @Override
    public void run() {
        register(data, Config.JOB_PATH + "/" + Config.WORKER_ID);
    }

    private CuratorFramework connect(){
        CuratorFramework zkClient;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(6000, 3);
        zkClient = CuratorFrameworkFactory
                .builder()
                .connectString(Config.ZOOKEEPER_ADDRESS)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(25000)
                .connectionTimeoutMs(20000)
                .build();
        zkClient.start();
        return zkClient;
    }

    private String createZnode(String data, String nodePath) {
        try {
            return zkClient.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(nodePath, data.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private void register(String data, String path){
        if (data != null && data.length() > 0) {
            String flag = createZnode(data, path);
            if (flag != null && data.contains(path)) {
                logger.info("Create znode {} successfull", flag);
            }
        }
    }
}
