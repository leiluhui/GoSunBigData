package com.hzgc.compare.worker.persistence;


import com.hzgc.compare.worker.conf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 *
 */
public abstract class FileReader {
    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);
    Config conf;
    String path;
    ExecutorService pool;
    int excutors = 12;

    FileReader() {
    }

    /**
     * 项目启动时，从本地文件中加载数据到内存
     */
    public abstract void loadRecord() throws IOException;

}
