package com.hzgc.service.collect.service;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import java.util.List;

public interface DiscoverCallBack {
    /**
     * 当监听节点下的数据发生变化会调用此方法
     *
     * @param currentData
     */
    public void run(List <ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException;
}
