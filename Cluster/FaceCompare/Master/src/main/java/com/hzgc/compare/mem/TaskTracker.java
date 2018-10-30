package com.hzgc.compare.mem;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.compare.conf.Config;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskTracker implements Serializable{
//    private static final Logger logger = LoggerFactory.getLogger(TaskTracker.class);
    private static Logger logger = Logger.getLogger(TaskTracker.class);
    private final int maxJobsPerTaskTracker = Config.WORKER_NUM_PER_TRACKER;
    private List<String> ports; //可用的端口号
    private List<Job> jobs;
    private String nodeGroup;

    TaskTracker(String nodeGroup){
        jobs = new ArrayList<>();
        this.nodeGroup = nodeGroup;
        ports = new ArrayList<>();
        ports.addAll(Arrays.asList(Config.PORTS.split(",")));
    }

    public Job changePort(Job job){
        logger.info("Chhange the port for job : " + job.getTaskId());
        String portUsed = job.getParam("port");
        if(ports.size() > 0){
            String portToUse = ports.get(0);
            ports.remove(0);
            job.setParam("port", portToUse);
            ports.add(portUsed);
        }
        return job;
    }

    boolean addJob(Job job){
        if(jobs.size() >= maxJobsPerTaskTracker){
            return false;
        }

        if(ports.size() <= 0){
            return false;
        }
        jobs.add(job);
        return true;
    }

    public List<String> getPorts() {
        return ports;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public int getJobCanBeAdd(){
        return maxJobsPerTaskTracker - jobs.size();
    }

    public List<Job> getJobs(){
        return jobs;
    }

//    public void disablePort(String port){
//        ports.remove(port);
//    }
}
