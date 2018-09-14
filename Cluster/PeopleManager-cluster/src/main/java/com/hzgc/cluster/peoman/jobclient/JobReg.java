package com.hzgc.cluster.peoman.jobclient;

import com.hzgc.cluster.peoman.zk.JobRegister;

public class JobReg {
    public static void main(String[] args) throws InterruptedException {
        String zkPath = "172.18.18.100";
        JobRegister jobRegister = new JobRegister(zkPath);
        jobRegister.regist("0_4","full");
    }
}
