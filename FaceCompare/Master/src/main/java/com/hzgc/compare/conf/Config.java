package com.hzgc.compare.conf;

import com.hzgc.compare.util.PropertiesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    public static String PORTS;
    public static final String JOB_PATH = "/worker_job";
    public static List<String> WORKER_ID_LIST;
    public static String ZK_ADDRESS;
    public static Integer MASTER_PORT;
    public static String MASTER_IP;
    public static int WORKER_NUM_PER_TRACKER;
    public static String CLUSTER_NAME ;
    public static String TASKTRACKER_PATH_ZK;
    public static long TIME_CHECK_JOB;
    public static final String TRACKER_PATH = "trackers.txt";

    static {
        Properties prop = PropertiesUtil.getProperties();
        PORTS = prop.getProperty("worker.rpc.port.list");
        String workerIds = prop.getProperty("worker.id.list");
        WORKER_ID_LIST = Arrays.asList(workerIds.split(","));
        ZK_ADDRESS = prop.getProperty("zookeeper.address");
        MASTER_PORT = Integer.parseInt(prop.getProperty("master.rpc.port"));
        MASTER_IP = prop.getProperty("master.ip");
        WORKER_NUM_PER_TRACKER = Integer.parseInt(prop.getProperty("worker.num.per.tracker"));
        CLUSTER_NAME = prop.getProperty("cluster.name");
        TASKTRACKER_PATH_ZK = "/LTS/" + CLUSTER_NAME + "/NODES/TASK_TRACKER";
        TIME_CHECK_JOB = Long.parseLong(prop.getProperty("time.check.job"));
    }

}
