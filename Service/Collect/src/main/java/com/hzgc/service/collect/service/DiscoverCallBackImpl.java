package com.hzgc.service.collect.service;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscoverCallBackImpl implements DiscoverCallBack {
    @Autowired
    FtpPersistence ftpPersistence;

    @Override
    public void run(List <ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException {
        String ftpPath = event.getData().getPath();
        //数据库的存储
        ftpPersistence.queryDataBase(ftpPath);
    }
}
