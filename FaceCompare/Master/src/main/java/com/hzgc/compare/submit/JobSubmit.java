package com.hzgc.compare.submit;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JobSubmit {
    private static final Logger logger = LoggerFactory.getLogger(JobSubmit.class);

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
        jobClient.submitJob(job);

    }

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
    }
}
