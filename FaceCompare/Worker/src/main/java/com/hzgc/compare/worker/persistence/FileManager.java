package com.hzgc.compare.worker.persistence;


import com.hzgc.compare.worker.common.tuple.Triplet;

import java.util.List;

/**
 * FilterManager主要用于管理本地文件（或HDFS文件），主要作用是内存持久化，本地文件的创建和删除
 */
public interface FileManager {

    /**
     * FileManager初始化
     */
    void init();

    /**
     * 获取当前buffer数据，持久化
     */
    void flush();

    /**
     * 获取当前buffer数据，持久化
     */
    void flush(List<Triplet<String, String, byte[]>> buffer);

    /**
     * 启动定期任务，检查文件是否存在过期，并删除过期文件，以及对应的HBase数据
     */
    void checkFile();

    void checkTaskTodo();

}
