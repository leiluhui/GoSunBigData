package com.hzgc.service.collect.service;

import com.hzgc.common.util.zookeeper.Curator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class JobDiscover implements Serializable {
    @Autowired
    FtpPersistence ftpPersistence;
    private CuratorFramework zkClient;
    private Curator curator;
    private final String ftp_register_path = "/ftp_register";

    public JobDiscover(@Value("${zk.address}") String zkAddress) {
        jobDiscover(zkAddress, 12000, 12000);
    }

    public void jobDiscover(String zkAddress, int sessionTimeOut, int connectionTimeOut) {
        Curator curator = new Curator(zkAddress, sessionTimeOut, connectionTimeOut);
        this.curator = curator;
        zkClient = curator.getClient();
        log.info("Start JobDiscover successful, zkAddress:?, sessionTimeOut:?, connectionTimeOut:?",
                zkAddress, sessionTimeOut, connectionTimeOut);
        startListener(curator.getClient());
    }

    public void startListener(CuratorFramework curatorFramework) {
        final PathChildrenCache pathCache = new PathChildrenCache(zkClient, ftp_register_path, true);
        try {
            pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            log.info("Start listen path: ?", ftp_register_path);
            pathCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        refreshData(pathCache.getCurrentData(),event);
                        JobDiscover.saveDatabase();
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //刷新数据库
    private void refreshData(List<ChildData> currentData, PathChildrenCacheEvent event) {
        String ftpPath = event.getData().getPath();
        //数据库的存储
        ftpPersistence.queryDataBase(ftpPath);
    }

    private static void saveDatabase() {
    }
}
