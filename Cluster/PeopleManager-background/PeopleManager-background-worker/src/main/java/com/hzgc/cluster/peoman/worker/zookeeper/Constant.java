package com.hzgc.cluster.peoman.worker.zookeeper;

import org.springframework.stereotype.Component;

@Component
public interface Constant {
    public String rootPath = "/peoman";
    public String tempPath = "/node";
}
