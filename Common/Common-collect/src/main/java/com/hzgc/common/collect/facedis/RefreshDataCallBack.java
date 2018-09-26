package com.hzgc.common.collect.facedis;

import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;

public interface RefreshDataCallBack {
    public void run(List<ChildData> childDataList);
}
