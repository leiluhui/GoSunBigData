package com.hzgc.common.service.peoman;

public interface JobRunnerProtocol {
    //请求参数
    public String STOP = "stop";
    public String RUN = "run";
    public String OFFSET = "offset";
    public String LIMIT = "limit";

    //响应参数
    public String ALREADYRUN = "alreadyrun";
    public String SUCCESSFULL = "successfull";
}
