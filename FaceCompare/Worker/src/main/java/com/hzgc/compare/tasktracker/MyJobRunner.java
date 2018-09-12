package com.hzgc.compare.tasktracker;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class MyJobRunner  implements JobRunner{
    private static final Logger logger = LoggerFactory.getLogger(MyJobRunner.class);

    public Result run(JobContext jobContext) throws Throwable {
        Job job = jobContext.getJob();
        String workId = job.getParam("workerId");
        String port = job.getParam("port");
        String nodeGroup = jobContext.getJob().getTaskTrackerNodeGroup();
        ProcessBuilder builder = new ProcessBuilder();

        String jarPath = MyJobRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File temp = new File(jarPath);
        String parentPath = temp.getParentFile().getParentFile().getPath();
        logger.info("---------------------------------start a worker---------------------------------");
        logger.info("sh " + parentPath + "/bin/start-worker.sh " + workId + " " + nodeGroup + " " + port + " " + job.getTaskId());
        builder.command("sh", parentPath + "/bin/start-worker.sh", workId, nodeGroup, port, job.getTaskId());
        builder.start();
        logger.info("--------------------------------------------------------------------------------");
        return null;
    }

    public static void main(String args[]){
//        String pathTemp = MyJobRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println(pathTemp);
    }
}
