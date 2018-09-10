package com.hzgc.common.rpc.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    protected CuratorFramework zkClient;
    private String zkAddress;
    private Constant constant;

    protected ZookeeperClient(String zkAddress, Constant constant) {
        this.zkAddress = zkAddress;
        this.constant = constant;
        this.zkClient = connectZookeeper();
    }

    public Constant getConstant() {
        return constant;
    }

    private CuratorFramework connectZookeeper() {
        CuratorFramework zkClient;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(RpcConfig.getCuratorBaseSleepTime(), RpcConfig.getCuratorMaxRetries());
        zkClient = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(RpcConfig.getZkSessionTime())
                .connectionTimeoutMs(RpcConfig.getZkConnectTime())
                .build();
        zkClient.start();
        try {
            zkClient.checkExists().forPath(constant.getRootPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Connect zookeeper successfull, zk address is {} ", zkAddress);
        return zkClient;
    }

    public void stop() {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
