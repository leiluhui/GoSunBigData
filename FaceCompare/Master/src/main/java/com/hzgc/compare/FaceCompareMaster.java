package com.hzgc.compare;

import com.hzgc.common.rpc.server.RpcServer;
import com.hzgc.common.rpc.server.zk.ServiceRegistry;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.conf.Config;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import com.hzgc.compare.submit.JobSubmit;
import com.hzgc.compare.zk.ZookeeperClient;

import java.util.List;

public class FaceCompareMaster {
    private TaskTrackerManager taskTrackerManager;

    private void init(){
        taskTrackerManager = TaskTrackerManager.getInstance();
        new ZookeeperClient(Config.ZK_ADDRESS);
    }

    private void start(){
        List<String> workerIds = Config.WORKER_ID_LIST;
        List<TaskTracker> list = taskTrackerManager.getTrackers();
        int index = 0;
        for(String workerId : workerIds){
            int trackerIndex = index % list.size();
            JobSubmit.submitJob(workerId, list.get(trackerIndex).getNodeGroup());
            index ++;
        }
        new RPCRegistry().run();
    }


    public static void main(String args[]) throws InterruptedException {
        FaceCompareMaster master = new FaceCompareMaster();
        master.init();
        master.start();
    }
}

class RPCRegistry implements Runnable{

    @Override
    public void run() {
        Constant constant = new Constant("/compare", "master");
        ServiceRegistry registry = new ServiceRegistry(Config.ZK_ADDRESS, constant);
        RpcServer rpcServer = new RpcServer(Config.MASTER_IP,
                Config.MASTER_PORT, registry);
        rpcServer.start();
    }
}