package com.hzgc.cluster.peoman.client.zookeeper;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscoveCallBack {
    /**
     * 当监听节点下的数据发生变化会调用此方法
     * @param currentData
     */
    public void run(List<ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException;
}
