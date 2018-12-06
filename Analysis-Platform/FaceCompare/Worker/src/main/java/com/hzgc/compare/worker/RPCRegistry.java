package com.hzgc.compare.worker;

import com.hzgc.common.rpc.server.RpcServer;
import com.hzgc.common.rpc.server.zk.ServiceRegistry;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.worker.conf.Config;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RPCRegistry implements Runnable{
    private static Logger log = Logger.getLogger(RPCRegistry.class);
    private ServiceRegistry registry;
    private String workerPathOnZk;

    RPCRegistry(String workerId, String port){
        this.workerPathOnZk = workerId;
        Constant constant = new Constant(Config.JOB_PATH, workerPathOnZk, CreateMode.EPHEMERAL);
        Map<String, String> param = new HashMap<>();
        param.put("workerId", workerId);
        param.put("port", port);
        constant.setParam(param);
        constant.setExitIfFaild(true);
        registry = new ServiceRegistry(Config.ZOOKEEPER_ADDRESS, constant);
        log.info("To Create node on zookeeper , node name " + workerPathOnZk + " , port " + port);
    }

    @Override
    public void run() {
        log.info("Registry the service.");
        if(Config.WORKER_ADDRESS == null){
            log.error("Get local ip address faild .");
            System.exit(1);
        }
        RpcServer rpcServer = new RpcServer(Config.WORKER_ADDRESS,
                Config.WORKER_RPC_PORT, registry);
        rpcServer.start();
        System.out.println("To create zookeeper node : " + workerPathOnZk);
    }

    /**
     * 检查是否注册成功
     * @return
     */
    public boolean checkJob(){
        List<String> children;
        try {
            children = registry.getConnect().getChildren().forPath(Config.JOB_PATH);
            log.info("The FaceCompareWorker on Zookeeper : " + children.toString());
            return children.contains(Config.WORKER_ID);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
