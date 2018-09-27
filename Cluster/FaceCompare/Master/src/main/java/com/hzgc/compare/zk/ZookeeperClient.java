package com.hzgc.compare.zk;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.compare.conf.Config;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import com.hzgc.compare.submit.JobSubmit;
import com.hzgc.compare.util.JobUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import java.util.*;

public class ZookeeperClient {
//    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    private static Logger logger = Logger.getLogger(ZookeeperClient.class);
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
            logger.info("Save TaskTrackers founded on zk.");
            TaskTrackerManager managet = TaskTrackerManager.getInstance();
            List<String> pathes = zkClient.getChildren().forPath(Config.TASKTRACKER_PATH_ZK);
            for(String pathName : pathes){
                String nodeGroup = analysePath(pathName);
                if(nodeGroup != null) {
                    managet.addTracker(nodeGroup);
                }
            }
            watchTaskTracker(zkClient);
//            watchJob(zkClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Connect zookeeper successfull, zk address is " + zkAddress);
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
            logger.info("Child event [type:" + event.getType() + ", path: " + (event.getData() != null ? event.getData().getPath() : null) + "]");
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
//                    logger.info("Add job " + workerId + " to Master.");
//                    taskTracker.getPorts().remove(port);
//                    taskTracker.getJobs().add(job);
                    break;
                case CHILD_REMOVED:
//                    logger.info("Renove job " + workerId + " from Master.");
//                    taskTracker.getPorts().add(port);
//                    List<Job> jobs = taskTracker.getJobs();
//                    Job temp = new Job();
//                    for(Job j : jobs){
//                        if(j.getTaskId().equals(taskId)){
//                            temp = j;
//                        }
//                    }
//                    jobs.remove(temp);
//                    JobSubmit.submitJob(job);
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
            logger.info("Child event [type:" + event.getType() + ", path: " + (event.getData() != null ? event.getData().getPath() : null) + "]");
            switch (event.getType()) {
                case CHILD_ADDED:
                    String nodeGroup = analysePath(event.getData().getPath());
                    if(nodeGroup != null && !managet.containe(nodeGroup)){
                        managet.addTracker(nodeGroup);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private String analysePath(String pathName){
        if(pathName == null || !pathName.contains("clusterName=" + Config.CLUSTER_NAME)){
            return null;
        }
        String temp = pathName.substring(pathName.indexOf("group=") + 6);
        String nodeGroup = temp.substring(0, temp.indexOf("&"));
        return nodeGroup;
    }

    private List<Job> getJobsOnZk(CuratorFramework zkClient) throws Exception {
        List<String> childPathes;
        List<Job> res = new ArrayList<>();
        try {
            childPathes = zkClient.getChildren().forPath(Config.JOB_PATH);
        } catch (Exception e){
            logger.warn("Get job On zookeeper faild ." + e.getMessage());
            return res;
        }
        for(String path : childPathes){
//            logger.info("Job on zk Path : " + Config.JOB_PATH + "/" + path);
            String data = new String(zkClient.getData().forPath(Config.JOB_PATH + "/" + path));
            Map param = JobUtil.jsonToMap(data);
            String workerId = (String) param.get("workerId");
            String nodeGroup = (String) param.get("nodeGroup");
            String port = (String) param.get("port");
            String taskId = (String) param.get("taskId");
            TaskTracker taskTracker = TaskTrackerManager.getInstance().getTaskTracker(nodeGroup);
            Job job = new Job();
            job.setTaskId(taskId);
            job.setParam("port", port);
            job.setParam("workerId", workerId);
            job.setPriority(100);
            job.setTaskTrackerNodeGroup(taskTracker.getNodeGroup());
            res.add(job);
//            taskTracker.getPorts().remove(port);
//            taskTracker.getJobs().add(job);
        }
        return res;
    }

    /**
     * 检测内存中的Job是否都存在于ZK上，如果不存在，则启动那个Job
     * @throws Exception
     */
    private void checkJobs() throws Exception {
        List<Job> jobsOnZk = getJobsOnZk(zkClient);
        List<Job> jobsOnMem = TaskTrackerManager.getInstance().getJobs();
        for(Job job : jobsOnZk){
            logger.debug("Job on zk workerId : " + job.getParam("workerId") + " taskId : " + job.getTaskId() + " nodeGroup : " + job.getTaskTrackerNodeGroup());
        }
        for(Job jobOnMem : jobsOnMem){
            boolean flag = true;
            for(Job jobOnZk : jobsOnZk){
                if(jobOnZk.getTaskId().equals(jobOnMem.getTaskId()) &&
                        jobOnZk.getTaskTrackerNodeGroup().equals(jobOnMem.getTaskTrackerNodeGroup()) &&
                        jobOnZk.getParam("workerId").equals(jobOnMem.getParam("workerId"))){
                    flag = false;
                }
            }
            if(flag){
                logger.warn("The job is not exist on zk , to start it. workerId : " + jobOnMem.getParam("workerId"));
                JobSubmit.submitJob(jobOnMem);
            }
        }
    }

    public void startToCheckJob(){
        new Timer().schedule(new TimeToCheckJob(), 1000 * 30, Config.TIME_CHECK_JOB);
    }

    class TimeToCheckJob extends TimerTask {
        @Override
        public void run() {
            try {
                logger.info("To Check died jobs.");
                checkJobs();
            } catch (Exception e) {
                logger.warn(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}