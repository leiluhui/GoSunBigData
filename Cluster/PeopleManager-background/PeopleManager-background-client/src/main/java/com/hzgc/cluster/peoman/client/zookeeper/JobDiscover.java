package com.hzgc.cluster.peoman.client.zookeeper;

import com.hzgc.common.util.zookeeper.Curator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class JobDiscover implements Serializable {
    private CuratorFramework zkClient;
    private Curator curator;

    public void JobDiscover(String zkAddress, int sessionTimeOut, int connectionTimeOut) {
        Curator curator = new Curator(zkAddress, sessionTimeOut, connectionTimeOut);
        this.curator = curator;
        zkClient = curator.getClient();
        log.info("Start JobDiscover successfull, zkAddress:?, sessionTimeOut:?, connectionTimeOut:?",
                zkAddress, sessionTimeOut, connectionTimeOut);
    }

    public JobDiscover(@Value("${zookeeper.connection.address}")String zkAddress) {
        JobDiscover(zkAddress,12000,12000);
    }

    /**
     * 启动监听时注册回调方法,用来触发实际的业务逻辑
     *
     * @param callBack 回调对象,其中的run方法封装了具体的业务逻辑
     */
    public void startListen(final DiscoveCallBack callBack, String path) throws InterruptedException {
        createRootPath(Constant.tempPath);
        createRootPath(Constant.rootPath);
        final PathChildrenCache pathCache = new PathChildrenCache(zkClient, path, true);
        try {
            pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            log.info("Start listen path: ?", path);
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
    private void createRootPath(String path) {
        //节点不存在则创建
        if (!curator.nodePathExists(path)) {
            curator.createNode(path, null, CreateMode.PERSISTENT);
        }
    }

    public List<String> listGroup(String path){
        return curator.getParenNodePath(path);
    }

    public boolean isExist(String path){
        return curator.nodePathExists(path);
    }

    public String getData(String path){
        byte[] data = curator.getNodeData(path);
        if (data == null){
            return "";
        }
        return new String(data);
    }
}