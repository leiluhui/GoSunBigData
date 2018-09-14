package com.hzgc.compare.worker;

import com.hzgc.common.rpc.server.RpcServer;
import com.hzgc.common.rpc.server.zk.ServiceRegistry;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.worker.conf.Config;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RPCRegistry implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RPCRegistry.class);
    private String workerId;
    private String nodeGroup;
    private String port;
    private String taskId;

    RPCRegistry(String workerId, String nodeGroup, String port, String taskId){
        this.workerId = workerId ;
        this.nodeGroup = nodeGroup ;
        this.port = port ;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        logger.info("Registry the service.");
        Constant constant = new Constant(Config.JOB_PATH, workerId, CreateMode.EPHEMERAL);
        Map<String, String> param = new HashMap<>();
        param.put("workerId", workerId);
        param.put("nodeGroup", nodeGroup);
        param.put("port", port);
        param.put("taskId", taskId);
        constant.setParam(param);
        constant.setExitIfFaild(true);
        ServiceRegistry registry = new ServiceRegistry(Config.ZOOKEEPER_ADDRESS, constant);
        RpcServer rpcServer = new RpcServer(Config.WORKER_ADDRESS,
                Config.WORKER_RPC_PORT, registry);
        rpcServer.start();
    }
}
