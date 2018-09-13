package com.hzgc.cluster.peoman.jobclient;

import com.hzgc.cluster.peoman.zk.JobRegister;

public class JobUpdate {
    public static void main(String[] args) throws Exception {
        String zkPath = "172.18.18.100";
        JobRegister jobRegister = new JobRegister(zkPath);
        jobRegister.update("/peoplemanager/10_14","full");
    }
}
