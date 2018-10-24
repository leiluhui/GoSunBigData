package com.hzgc.compare.server;

import com.hzgc.common.rpc.client.result.AllReturn;

import java.util.List;

public interface MasterServer {
    void submitJob(String workerId);

    void submitJob(String workerId, String taskTrackerGroup);

    AllReturn<List<String>> getJobsNow();
}
