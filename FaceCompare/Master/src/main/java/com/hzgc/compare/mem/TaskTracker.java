package com.hzgc.compare.mem;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.compare.conf.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskTracker implements Serializable{
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

    int getJobCanBeAdd(){
        return maxJobsPerTaskTracker - jobs.size();
    }

    public List<Job> getJobs(){
        return jobs;
    }
}
