package com.hzgc.cluster.peoman.jobclient;

import com.hzgc.cluster.peoman.zk.JobDiscover;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobCallBack {
    public static void main(String[] args) throws InterruptedException {
        String zkPath = "172.18.18.100";
        JobDiscover jobDiscover = new JobDiscover(zkPath,10000,10000);
        JobClientCallBack jobClientCallBack = new JobClientCallBack();
        jobDiscover.startListen(jobClientCallBack);
        Thread.sleep(1000000);
    }
}
