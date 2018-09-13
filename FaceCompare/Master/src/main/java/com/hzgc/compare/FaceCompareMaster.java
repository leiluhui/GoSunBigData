package com.hzgc.compare;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.common.rpc.server.RpcServer;
import com.hzgc.common.rpc.server.zk.ServiceRegistry;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.conf.Config;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import com.hzgc.compare.submit.JobSubmit;
import com.hzgc.compare.zk.ZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceCompareMaster {
    private static final Logger logger = LoggerFactory.getLogger(FaceCompareMaster.class);
    private TaskTrackerManager taskTrackerManager;
    private ZookeeperClient zkClient;

    private void init(){
        taskTrackerManager = TaskTrackerManager.getInstance();
        taskTrackerManager.loadTackers();
        zkClient = new ZookeeperClient(Config.ZK_ADDRESS);
    }

    private void start(){
        startWorker();
        zkClient.startToCheckJob();
    }

    private void startWorker(){
        List<String> workerIds = Config.WORKER_ID_LIST;
        List<String> workerToStart = new ArrayList<>();
        List<Job> jobs = TaskTrackerManager.getInstance().getJobs();
        for(String workerId : workerIds){
            boolean flag = true;
            for(Job job : jobs){
                if(workerId.equals(job.getParam("workerId"))){
                    flag = false;
                }
            }
            if (flag) {
                workerToStart.add(workerId);
                logger.info("To start worker , workerId : " + workerId);
            }
        }
        List<TaskTracker> list = taskTrackerManager.getTrackers();
        int index = 0;
        for(String workerId : workerToStart){
            int trackerIndex = index % list.size();
            JobSubmit.submitJob(workerId, list.get(trackerIndex).getNodeGroup());
            index ++;
        }
        Thread thread = new Thread(new RPCRegistry());
        thread.start();
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
        Constant constant = new Constant("/compare/master", "master");
        ServiceRegistry registry = new ServiceRegistry(Config.ZK_ADDRESS, constant);
        RpcServer rpcServer = new RpcServer(Config.MASTER_IP,
                Config.MASTER_PORT, registry);
        rpcServer.start();
    }
}