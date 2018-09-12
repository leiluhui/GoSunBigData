package com.hzgc.compare.tasktracker;

import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.TaskTracker;
import com.hzgc.compare.worker.conf.Config;

public class TaskTrackerStart {
    public static void main(String args[]){
        LoggerFactory.setLoggerAdapter("slf4j");
        TaskTracker taskTracker = new TaskTracker();
        taskTracker.addConfig("zk.client", "zkclient");
        taskTracker.addConfig("lts.remoting", "netty");
        taskTracker.addConfig("lts.json", "jackson");
        taskTracker.addConfig("job.fail.store", "mapdb");
        taskTracker.setJobRunnerClass(MyJobRunner.class);
        taskTracker.setRegistryAddress("zookeeper://" + Config.ZOOKEEPER_ADDRESS);
        taskTracker.setClusterName(Config.CLUSTER_NAME);
        taskTracker.setNodeGroup(Config.TRACKER_GROUP);
        taskTracker.setWorkThreads(20);
        taskTracker.start();
    }
}
