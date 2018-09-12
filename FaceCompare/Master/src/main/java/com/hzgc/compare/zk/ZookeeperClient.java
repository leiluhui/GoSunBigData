package com.hzgc.compare.zk;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.compare.conf.Config;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import com.hzgc.compare.submit.JobSubmit;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    private CuratorFramework zkClient;
    private String zkAddress;

    public ZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        this.zkClient = connectZookeeper();
    }

    private CuratorFramework connectZookeeper() {
        CuratorFramework zkClient;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(6000, 3);
        zkClient = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(25000)
                .connectionTimeoutMs(20000)
                .build();
        zkClient.start();
        try {
            TaskTrackerManager managet = TaskTrackerManager.getInstance();
            List<String> pathes = zkClient.getChildren().forPath(Config.TASKTRACKER_PATH_ZK);
            for(String pathName : pathes){
                String nodeGroup = analysePath(pathName);
                logger.info("Add nodeGroup {} to Master." + nodeGroup);
                if(nodeGroup != null) {
                    managet.addTracker(nodeGroup);
                }
            }
//            watchTaskTracker(zkClient);
            watchJob(zkClient);
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

    private void watchJob(CuratorFramework zkClient) throws Exception {
        PathChildrenCache pathCache =
                new PathChildrenCache(zkClient, Config.JOB_PATH, true);
        pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathCache.getListenable().addListener((client, event) -> {
            logger.info("Child event [type:{}, path:{}]",
                    event.getType(),
                    event.getData() != null ? event.getData().getPath() : null);
            String data = new String(event.getData().getData());
            System.out.println(data);
            String workerId = data.split(",")[0];
            String nodeGroup = data.split(",")[1];
            String port = data.split(",")[2];
            String taskId = data.split(",")[3];
            TaskTracker taskTracker = TaskTrackerManager.getInstance().getTaskTracker(nodeGroup);
            Job job = new Job();
            job.setTaskId(taskId);
            job.setParam("port", port);
            job.setParam("workerId", workerId);
            job.setPriority(100);
            job.setTaskTrackerNodeGroup(taskTracker.getNodeGroup());
            switch (event.getType()) {
                case CHILD_ADDED:
                    logger.info("Add job " + workerId + " to Master.");
                    taskTracker.getPorts().remove(port);
                    taskTracker.getJobs().add(job);
                    break;
                case CHILD_REMOVED:
                    logger.info("Renove job " + workerId + " from Master.");
                    taskTracker.getPorts().add(port);
                    List<Job> jobs = taskTracker.getJobs();
                    Job temp = new Job();
                    for(Job j : jobs){
                        if(j.getTaskId().equals(taskId)){
                            temp = j;
                        }
                    }
                    jobs.remove(temp);
                    JobSubmit.submitJob(workerId, nodeGroup);
                    break;
                default:
                    break;
            }
        });
    }

    private void watchTaskTracker(CuratorFramework zkClient) throws Exception {
        TaskTrackerManager managet = TaskTrackerManager.getInstance();
        PathChildrenCache pathCache =
                new PathChildrenCache(zkClient, Config.TASKTRACKER_PATH_ZK, true);
        //此种类型的StartMode意思为已存在节点不作为变化事件
        pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathCache.getListenable().addListener((client, event) -> {
            logger.info("Child event [type:{}, path:{}]",
                    event.getType(),
                    event.getData() != null ? event.getData().getPath() : null);
            switch (event.getType()) {
                case CHILD_ADDED:
                    List<String> nodeGroupsAdd = new ArrayList<>();
                    for(ChildData childData : pathCache.getCurrentData()){
                        String nodeName = childData.getPath().replace(Config.TASKTRACKER_PATH_ZK, "");
                        String nodeGroup = analysePath(nodeName);
                        if(!managet.containe(nodeGroup)){
                            nodeGroupsAdd.add(nodeGroup);
                        }
                    }
                    for(String nodeGroup : nodeGroupsAdd){
                        managet.addTracker(nodeGroup);
                    }
                    break;
                case CHILD_REMOVED:
                    List<String> nodeGroupsRemove = new ArrayList<>();
                    for(TaskTracker tracker : managet.getTrackers()){
                        nodeGroupsRemove.add(tracker.getNodeGroup());
                    }
                    for(ChildData childData : pathCache.getCurrentData()){
                        String nodeName = childData.getPath().replace(Config.TASKTRACKER_PATH_ZK, "");
                        String nodeGroup = analysePath(nodeName);
                        if(nodeGroupsRemove.contains(nodeGroup)){
                            nodeGroupsRemove.remove(nodeGroup);
                        }
                    }
                    for(String nodeGroup : nodeGroupsRemove){
                        managet.removeTracker(nodeGroup);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    public String analysePath(String pathName){
        if(pathName == null || !pathName.contains("clusterName=" + Config.CLUSTER_NAME)){
            return null;
        }
        String temp = pathName.substring(pathName.indexOf("group=") + 6);
        String nodeGroup = temp.substring(0, temp.indexOf("&"));
        return nodeGroup;
    }

    public static void main(String args[]) throws InterruptedException {
        ZookeeperClient client = new ZookeeperClient(Config.ZK_ADDRESS);
    }
}