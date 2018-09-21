package com.hzgc.compare.mem;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.compare.conf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class TaskTrackerManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskTrackerManager.class);
    private static TaskTrackerManager taskTrackerManager;
    private List<TaskTracker> trackers;
//    private Map<Job, Integer> numOfSend;

    private TaskTrackerManager(){
        trackers = new ArrayList<>();
//        numOfSend = new HashMap<>();
    }

    public static TaskTrackerManager getInstance(){
        if(taskTrackerManager == null){
            taskTrackerManager = new TaskTrackerManager();
        }
        return taskTrackerManager;
    }

    private void addTrackers(List<TaskTracker> taskTrackers){
        trackers.addAll(taskTrackers);
    }

    public void addTracker(String nodeGroup){
        for(TaskTracker tracker : trackers){
            if(tracker.getNodeGroup().equals(nodeGroup)){
                return;
            }
        }
        logger.info("Add a tracker , the node group is " + nodeGroup);
        TaskTracker tracker = new TaskTracker(nodeGroup);
        trackers.add(tracker);
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
            if(tracker.getNodeGroup().equals(nodeGroup)){
                return tracker;
            }
        }
        logger.info("There are no trackers free.");
        return null;
    }

    public List<TaskTracker> getTrackers() {
        return trackers;
    }

    public List<Job> getJobs(){
        List<Job> res = new ArrayList<>();
        for(TaskTracker tracker : trackers){
            res.addAll(tracker.getJobs());
        }
        return res;
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

    public void loadTackers(){
        ObjectInputStream ois;
        try {
            File file = new File(Config.TRACKER_PATH);
            if(!file.isFile()){
                return;
            }
            ois = new ObjectInputStream(new FileInputStream(Config.TRACKER_PATH));
            List<TaskTracker> list = (List<TaskTracker>) ois.readObject();
            ois.close();
            addTrackers(list);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Load taskTracker info from local faild. " + e.getMessage());
        }
    }

    public void saveTracker(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Config.TRACKER_PATH));
            oos.writeObject(trackers);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            logger.error("Save taskTracker info to local faild. " + e.getMessage());
        }
    }
//
//    /**
//     *  为给定Job增加一次发送次数
//     * @param job
//     */
//    public void addTimes(Job job){
//        Collection<Map.Entry<Job, Integer>> collection = new HashSet<>();
//        collection.addAll(numOfSend.entrySet());
//        for(Map.Entry<Job, Integer> entry : collection){
//            if(jobEquals(entry.getKey(), job)){
//                numOfSend.put(entry.getKey(), entry.getValue() + 1);
//            }else {
//                numOfSend.put(job, 1);
//            }
//        }
//    }
//
//    /**
//     * 删除给定Job的发送计数
//     * @param job
//     */
//    public void removeTimes(Job job){
//        Collection<Map.Entry<Job, Integer>> collection = new HashSet<>();
//        collection.addAll(numOfSend.entrySet());
//        for(Map.Entry<Job, Integer> entry : collection){
//            if(jobEquals(entry.getKey(), job)){
//                numOfSend.remove(entry.getKey());
//            }
//        }
//    }
//
//    /**
//     * 根据给定的job 获取发送次数
//     * @param job
//     * @return
//     */
//    public int getTimesOfJob(Job job){
//        for(Map.Entry<Job, Integer> entry : numOfSend.entrySet()){
//            if(jobEquals(entry.getKey(), job)){
//                return entry.getValue();
//            }
//        }
//        return -1;
//    }
//
//    private boolean jobEquals(Job job1, Job job2){
//        return job1.getParam("workerId").equals(job2.getParam("workerId"))
//                && job1.getTaskTrackerNodeGroup().equals(job2.getTaskTrackerNodeGroup());
//    }
//

}
