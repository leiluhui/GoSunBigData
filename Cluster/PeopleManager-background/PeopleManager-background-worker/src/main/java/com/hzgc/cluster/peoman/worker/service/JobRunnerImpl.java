package com.hzgc.cluster.peoman.worker.service;

import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;

@JobRunner4TaskTracker
@Slf4j
public class JobRunnerImpl implements JobRunner {

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        return null;
    }
}
