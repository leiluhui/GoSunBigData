package com.hzgc.compare.tasktracker;

import com.github.ltsopensource.core.domain.Job;
import java.util.HashMap;
import java.util.Map;

public class JobManager {
    private Map<Job, Long> jobToTime;
    private static JobManager jobManager;

    private JobManager(){
        jobToTime = new HashMap<>();
    }

    public static JobManager getInstance(){
        if(jobManager == null){
            jobManager = new JobManager();
        }
        return jobManager;
    }

    public Map<Job, Long> getJobs(){
        return jobToTime;
    }
}
