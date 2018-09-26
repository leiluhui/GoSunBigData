package com.hzgc.compare.submit;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import org.apache.log4j.Logger;

import java.util.List;

public class JobSubmit {
//    private static final Logger logger = LoggerFactory.getLogger(JobSubmit.class);
    private static Logger logger = Logger.getLogger(JobSubmit.class);

    /**
     *  接收到增加Worker的命令时
     * @param workerId 要增加的workerId
     */
    public static void submitJob(String workerId) {
        JobClient jobClient = JobClientUtil.getClient();
        TaskTracker taskTracker = TaskTrackerManager.getInstance().choseTaskTracker();
        if(taskTracker == null){
            return;
        }
        List<String> ports = taskTracker.getPorts();
        if(ports.size() <= 0){
            return;
        }
        logger.info("Submit job : " + workerId + " To tracker group : " + taskTracker.getNodeGroup());
        String port = taskTracker.getPorts().remove(0);
        Job job = new Job();
        job.setTaskId(workerId);
        job.setParam("port", port);
        job.setParam("workerId", workerId);
        job.setPriority(100);
        job.setTaskTrackerNodeGroup(taskTracker.getNodeGroup());
        job.setMaxRetryTimes(0);
        jobClient.submitJob(job);
//        TaskTrackerManager.getInstance().addTimes(job);

        //更新内存中的Job
        taskTracker.getPorts().remove(port);
        taskTracker.getJobs().add(job);
        TaskTrackerManager.getInstance().saveTracker();
    }

    public static void submitJob(Job job){
        JobClient jobClient = JobClientUtil.getClient();
        String taskTrackerGroup = job.getTaskTrackerNodeGroup();
        TaskTracker taskTracker = TaskTrackerManager.getInstance().getTaskTracker(taskTrackerGroup);

        if(taskTracker == null){
            System.out.println("There is no TaskTracker : " + taskTrackerGroup + "free.");
            return;
        }
        List<String> ports = taskTracker.getPorts();
        if(ports.size() <= 0){
            System.out.println("There is no TaskTracker : " + taskTrackerGroup + "free.");
            return;
        }
        job = taskTracker.changePort(job);
        logger.info("Submit job : " + job.getParam("workerId") + " To tracker group : " + taskTrackerGroup);
        jobClient.submitJob(job);
//        TaskTrackerManager.getInstance().addTimes(job);
        TaskTrackerManager.getInstance().saveTracker();
    }

    /**
     * 项目启动时
     * @param workerId 要启动的workerId
     * @param nodeGroup 启动worker的节点组
     */
    public static void submitJob(String workerId, String nodeGroup){
        JobClient jobClient = JobClientUtil.getClient();
        TaskTracker taskTracker = TaskTrackerManager.getInstance().getTaskTracker(nodeGroup);
        if(taskTracker == null){
            System.out.println("There is no TaskTracker : " + nodeGroup + "free.");
            return;
        }
        List<String> ports = taskTracker.getPorts();
        if(ports.size() <= 0){
            System.out.println("There is no TaskTracker : " + nodeGroup + "free.");
            return;
        }
        logger.info("Submit job : " + workerId + " To tracker group : " + taskTracker.getNodeGroup());
        String port = taskTracker.getPorts().remove(0);
        Job job = new Job();
        job.setTaskId(workerId);
        job.setParam("port", port);
        job.setParam("workerId", workerId);
        job.setPriority(100);
        job.setTaskTrackerNodeGroup(taskTracker.getNodeGroup());
        jobClient.submitJob(job);
//        TaskTrackerManager.getInstance().addTimes(job);

        //更新内存中的Job
        taskTracker.getPorts().remove(port);
        taskTracker.getJobs().add(job);
        TaskTrackerManager.getInstance().saveTracker();
    }
}
