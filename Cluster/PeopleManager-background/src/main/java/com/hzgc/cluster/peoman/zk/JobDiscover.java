package com.hzgc.cluster.peoman.zk;

import com.hzgc.common.zookeeper.Curator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;

import java.io.Serializable;

@Slf4j
public class JobDiscover implements Serializable {
    private CuratorFramework zkClient;
    private Curator curator;

    public JobDiscover(String zkAddress, int sessionTimeOut, int connectionTimeOut) {
        Curator curator = new Curator(zkAddress, sessionTimeOut, connectionTimeOut);
        this.curator = curator;
        zkClient = curator.getClient();
        log.info("Start JobDiscover successfull, zkAddress:?, sessionTimeOut:?, connectionTimeOut:?",
                zkAddress, sessionTimeOut, connectionTimeOut);
    }

    public JobDiscover(String zkAddress) {
        this(zkAddress, 12000, 12000);
    }

    /**
     * 启动监听时注册回调方法,用来触发实际的业务逻辑
     *
     * @param callBack 回调对象,其中的run方法封装了具体的业务逻辑
     */
    public void startListen(final DiscoveCallBack callBack) throws InterruptedException {
        createRootPath();
        final PathChildrenCache pathCache = new PathChildrenCache(zkClient, Constant.rootPath, true);
        try {
            pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            log.info("Start listen path: ?", Constant.rootPath);
            pathCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        callBack.run(pathCache.getCurrentData(), event);
                        break;
                    case CHILD_UPDATED:
                        callBack.run(pathCache.getCurrentData(), event);
                        break;
                    case CHILD_REMOVED:
                        callBack.run(pathCache.getCurrentData(), event);
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        callBack.run(pathCache.getCurrentData(), null);
    }

    /**
     * 创建根路径
     */
    private void createRootPath() {
        //节点不存在则创建
        if (!curator.nodePathExists(Constant.rootPath)) {
            curator.createNode(Constant.rootPath, null, CreateMode.PERSISTENT);
        }
    }

    public boolean isExist(String path){
        boolean stat = curator.nodePathExists(path);
        return stat;
    }
}