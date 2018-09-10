package com.hzgc.common.rpc.server.zk;

import com.google.common.base.Strings;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.common.rpc.util.ZookeeperClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册工具，会将当前服务相关信息注册在zk节点上面
 */
public class ServiceRegistry extends ZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    public ServiceRegistry(String zkAddress, Constant constant) {
        super(zkAddress, constant);
    }

    public void register(String data, Constant constant) {
        if (!Strings.isNullOrEmpty(data)) {
            String flag = createZnode(data, constant);
            if (!Strings.isNullOrEmpty(flag) && data.contains(constant.getNodePath())) {
                logger.info("Create znode {} successfull", flag);
            }
        }
    }


    private String createZnode(String data, Constant constant) {
        try {
            return zkClient.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(constant.getNodePath(), data.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
