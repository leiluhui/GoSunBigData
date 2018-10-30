package com.hzgc.compare.server;

import com.github.ltsopensource.core.domain.Job;
import com.hzgc.common.rpc.client.result.AllReturn;
import com.hzgc.common.rpc.server.annotation.RpcService;
import com.hzgc.compare.mem.TaskTracker;
import com.hzgc.compare.mem.TaskTrackerManager;
import com.hzgc.compare.submit.JobSubmit;

import java.util.ArrayList;
import java.util.List;

@RpcService(MasterServer.class)
public class MasterServerImpl implements MasterServer {

    @Override
    public void submitJob(String workerId) {
        JobSubmit.submitJob(workerId);
    }

    @Override
    public void submitJob(String workerId, String taskTrackerGroup){
        JobSubmit.submitJob2(workerId, taskTrackerGroup);
    }

    @Override
    public AllReturn<List<String>> getJobsNow() {
        List<String> res = new ArrayList<>();
        List<TaskTracker> trackers = TaskTrackerManager.getInstance().getTrackers();
        for(TaskTracker tracker : trackers){
            for(Job job : tracker.getJobs()){
                String workerId = job.getParam("workerId");
                String port = job.getParam("port");
                String taskTrackerNodeGroup = job.getTaskTrackerNodeGroup();
                res.add("workerId : " + workerId + " , taskTrackerNodeGroup : " + taskTrackerNodeGroup + " , port : " + port
                + " , taskId : " + job.getTaskId());
            }
        }
        return new AllReturn<>(res);
    }
}
