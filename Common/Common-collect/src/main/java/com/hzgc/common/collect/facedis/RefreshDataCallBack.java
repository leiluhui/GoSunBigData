package com.hzgc.common.collect.facedis;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface RefreshDataCallBack {
    public void run(PathChildrenCacheEvent event);
}
