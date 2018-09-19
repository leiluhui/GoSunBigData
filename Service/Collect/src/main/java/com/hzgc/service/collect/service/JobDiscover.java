package com.hzgc.service.collect.service;

import com.hzgc.common.util.zookeeper.Curator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class JobDiscover implements Serializable {
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
    }

    public void startListener(final DiscoverCallBack callBack, String path) {
        final PathChildrenCache pathCache = new PathChildrenCache(zkClient, path, true);
        try {
            pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            log.info("Start listen path: ?", path);
            pathCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        callBack.run(pathCache.getCurrentData(), event);
                        JobDiscover.saveDatabase();
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveDatabase() {
    }
}
