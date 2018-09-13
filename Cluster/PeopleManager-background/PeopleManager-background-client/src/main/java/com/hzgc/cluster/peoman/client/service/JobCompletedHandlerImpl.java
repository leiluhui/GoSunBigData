package com.hzgc.cluster.peoman.client.service;

import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.core.domain.JobResult;
import com.github.ltsopensource.jobclient.support.JobCompletedHandler;
import com.github.ltsopensource.spring.boot.annotation.JobCompletedHandler4JobClient;

import java.util.List;

@JobCompletedHandler4JobClient
public class JobCompletedHandlerImpl implements JobCompletedHandler {
    @Override
    public void onComplete(List<JobResult> jobResults) {
        if (CollectionUtils.isNotEmpty(jobResults)) {

        }
    }
}
