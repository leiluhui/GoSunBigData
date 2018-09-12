package com.hzgc.compare.mem;

import com.github.ltsopensource.core.domain.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskTrackerManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskTrackerManager.class);
    private static TaskTrackerManager taskTrackerManager;
    private List<TaskTracker> trackers;
    private Map<String, List<Job>> tempMap;

    private TaskTrackerManager(){
        trackers = new ArrayList<>();
        tempMap = new HashMap<>();
    }

    public static TaskTrackerManager getInstance(){
        if(taskTrackerManager == null){
            taskTrackerManager = new TaskTrackerManager();
        }
        return taskTrackerManager;
    }

    public void addTracker(String nodeGroup){
        logger.info("Add a tracker , the node group is " + nodeGroup);
        List<Job> jobs = tempMap.get(nodeGroup);
        if(jobs != null && jobs.size() > 0){
            logger.info("There are some jobs should be run in the Tracker.");
            TaskTracker tracker = new TaskTracker(nodeGroup);
            for(Job job : jobs){
                tracker.addJob(job);
                String port = job.getParam("port");
                tracker.getPorts().remove(port);
            }
            trackers.add(tracker);
            tempMap.remove(nodeGroup);
        } else {
            trackers.add(new TaskTracker(nodeGroup));
        }
    }

    public void removeTracker(String nodeGroup){
        logger.info("Remove a tracker , the node group is " + nodeGroup);
        int index = 0;
        for(TaskTracker tracker : trackers){
            if(tracker.getNodeGroup().equals(nodeGroup)){
                break;
            }
            index ++;
        }
        trackers.remove(index);
        List<Job> jobs = trackers.get(index).getJobs();
        if(jobs.size() > 0){
            logger.info("There are some job run in the Tracker, save it");
            tempMap.put(nodeGroup, jobs);
        }
    }

    public TaskTracker choseTaskTracker(){
        TaskTracker taskTracker = null;
        int num = 0;
        for(TaskTracker tracker : trackers){
            if(tracker.getJobCanBeAdd() > num){
                num = tracker.getJobCanBeAdd();
                taskTracker = tracker;
            }
        }
        if(num == 0){
            return null;
        } else {
            logger.info("Chose a free tracker. There are {} job can be run int This tracker" , num);
            return taskTracker;
        }
    }

    public TaskTracker getTaskTracker(String nodeGroup){
        for(TaskTracker tracker : trackers){
            if(tracker.getNodeGroup().equals(nodeGroup) && tracker.getJobCanBeAdd() > 0){
                return tracker;
            }
        }
        logger.info("There are no trackers free.");
        return null;
    }

    public List<TaskTracker> getTrackers() {
        return trackers;
    }

    public boolean containe(String nodeGroup){
        if(nodeGroup == null){
            return false;
        }
        for(TaskTracker tracker : trackers){
            if(nodeGroup.equals(tracker.getNodeGroup())){
                return true;
            }
        }
        return false;
    }
}
