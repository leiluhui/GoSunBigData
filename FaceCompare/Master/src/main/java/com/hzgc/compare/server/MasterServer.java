package com.hzgc.compare.server;

import com.hzgc.common.rpc.client.result.AllReturn;

import java.util.List;

public interface MasterServer {
    void submitJob(String workerId);

    AllReturn<List<String>> getJobsNow();
}
