package com.hzgc.common.collect.facedis;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.zookeeper.Curator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;


import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FtpRegisterClient implements Serializable {

    private static final Logger LOG = Logger.getLogger(FtpRegisterClient.class);
    // ftp total register info
    private volatile List<FtpRegisterInfo> ftpRegisterInfoList = new CopyOnWriteArrayList<>();
    // face ftp total register info
    private volatile List<FtpRegisterInfo> faceFtpRegisterInfoList = new CopyOnWriteArrayList<>();
    // car ftp total register info
    private volatile List<FtpRegisterInfo> carFtpRegisterInfoList = new CopyOnWriteArrayList<>();
    // person ftp total register info
    private volatile List<FtpRegisterInfo> personFtpRegisterInfoList = new CopyOnWriteArrayList<>();
    // ftp ip and hostname mapping (key:hostname,value:ip)
    private volatile Map<String, String> ftpIpMapping = new ConcurrentHashMap<>();
    private final String ftp_register_path = "/ftp_register";
    private Curator registerClient;

    public FtpRegisterClient(String zkAddress) {
        registerClient = new Curator(zkAddress, 20000, 15000);
        if (registerClient.nodePathExists(ftp_register_path)) {
            LOG.info("Ftp register root path '/ftp_register' is exists");
        } else {
            registerClient.createNode(ftp_register_path, null, CreateMode.PERSISTENT);
            LOG.info("Ftp register root path '/ftp_register' create successfully");
        }
        initPathCache(registerClient.getClient());
    }

    public void createNode(FtpRegisterInfo registerInfo) {
        if (registerInfo != null && registerInfo.getFtpIPAddress() != null
                && registerInfo.getFtpIPAddress().length() > 0) {
            String nodePath = ftp_register_path + "/" + registerInfo.getFtpIPAddress();
            byte[] nodeData = JSONUtil.toJson(registerInfo).getBytes();
            if (registerClient.nodePathExists(nodePath)){
                registerClient.deleteChildNode(nodePath);
            }
            registerClient.createNode(nodePath, nodeData, CreateMode.EPHEMERAL);
            LOG.info("Create ftp register child node path: " + nodePath
                    + " successfully, data: " + JSONUtil.toJson(registerInfo));
        }
    }

    private void initPathCache(CuratorFramework curatorFramework) {
        final PathChildrenCache pathCache =
                new PathChildrenCache(curatorFramework, ftp_register_path, true);
        try {
            pathCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            pathCache.getListenable().addListener((client, event) -> {
                String log = event.getData() != null ? event.getData().getPath() : null;
                LOG.info("Ftp register child event [type:" + event.getType()
                        + ", path:" + log + "]");
                switch (event.getType()) {
                    case CHILD_ADDED:
                        refreshData(pathCache.getCurrentData());
                        break;
                    case CHILD_UPDATED:
                        refreshData(pathCache.getCurrentData());
                        break;
                    case CHILD_REMOVED:
                        refreshData(pathCache.getCurrentData());
                        break;
                    default:
                        break;
                }
            });
            //尝试第一次刷新节点下数据
            refreshData(pathCache.getCurrentData());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private void refreshData(List<ChildData> currentData) {
        if (currentData != null && currentData.size() > 0) {
            ftpRegisterInfoList.clear();
            faceFtpRegisterInfoList.clear();
            carFtpRegisterInfoList.clear();
            personFtpRegisterInfoList.clear();
            ftpIpMapping.clear();
            for (ChildData childData : currentData) {
                FtpRegisterInfo registerInfo =
                        JSONUtil.toObject(new String(childData.getData()), FtpRegisterInfo.class);
                ftpRegisterInfoList.add(registerInfo);
                if (registerInfo != null) {
                    switch (registerInfo.getFtpType()) {
                        case "face":
                            faceFtpRegisterInfoList.add(registerInfo);
                            break;
                        case "car":
                            carFtpRegisterInfoList.add(registerInfo);
                            break;
                        case "person":
                            personFtpRegisterInfoList.add(registerInfo);
                            break;
                        default:
                            LOG.error("Ftp type error for this ftp register info:"
                                    + registerInfo.getFtpIPAddress() + " = "
                                    + JSONUtil.toJson(registerInfo));
                            break;
                    }
                    ftpIpMapping.put(registerInfo.getFtpHomeName(), registerInfo.getFtpIPAddress());
                } else {
                    LOG.error("Ftp register info is null, ftp register child path:" + childData.getPath());
                }
            }

            LOG.info("*************************************************************");
            LOG.info("Total ftp register info:" + Arrays.toString(ftpRegisterInfoList.toArray()));
            LOG.info("Face ftp register info:" + Arrays.toString(faceFtpRegisterInfoList.toArray()));
            LOG.info("Car ftp register info:" + Arrays.toString(carFtpRegisterInfoList.toArray()));
            LOG.info("Person ftp register info:" + Arrays.toString(personFtpRegisterInfoList.toArray()));
            LOG.info("Ftp ip and hostname mapping:" + JSONUtil.toJson(ftpIpMapping));
            LOG.info("*************************************************************");
        }
    }

    public List<FtpRegisterInfo> getFtpRegisterInfoList() {
        return ftpRegisterInfoList;
    }

    public List<FtpRegisterInfo> getFaceFtpRegisterInfoList() {
        return faceFtpRegisterInfoList;
    }

    public List<FtpRegisterInfo> getCarFtpRegisterInfoList() {
        return carFtpRegisterInfoList;
    }

    public List<FtpRegisterInfo> getPersonFtpRegisterInfoList() {
        return personFtpRegisterInfoList;
    }

    public Map<String, String> getFtpIpMapping() {
        return ftpIpMapping;
    }
}
