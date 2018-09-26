package com.hzgc.cluster.peoman.worker.zookeeper;

import com.hzgc.common.util.zookeeper.Curator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

import java.io.Serializable;

@Slf4j
public class WorkerRegister implements Serializable {
    private Curator zkClient;

    /**
     *
     * @param zkAddress zookeeper地址
     * @param sessionTimeOut session超时时间
     * @param connectionTimeOut 连接超时时间
     */
    public WorkerRegister(String zkAddress, int sessionTimeOut, int connectionTimeOut) {
        zkClient = new Curator(zkAddress, sessionTimeOut, connectionTimeOut);
        log.info("Start JobRegister successfull, zkAddress:?, sessionTimeOut:?, connectionTimeOut:?",
                zkAddress, sessionTimeOut, connectionTimeOut);
    }

    /**
     * session超时和连接超时使用默认值
     *
     * @param zkAddress zookeeper地址
     */
    public WorkerRegister(String zkAddress) {
        this(zkAddress, 12000, 12000);
    }

    /**
     * 向Zookeeper注册当前节点信息
     *
     * @param registInfo 注册信息
     */
    public void regist(String registInfo) throws InterruptedException {
        String registPath = Constant.tempPath + "/" + registInfo;
        zkClient.createNode(registPath, null, CreateMode.EPHEMERAL);
    }

    public void update(String path,String Data) throws Exception{
        zkClient.setNodeDate(path,Data.getBytes());
    }
}
