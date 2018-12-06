package com.hzgc.compare.worker.persistence;

import com.hzgc.common.rpc.server.zk.GsonUtil;
import com.hzgc.compare.worker.conf.Config;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLockManager {
    private static Logger log = Logger.getLogger(LocalFileReader.class);
    private static FileLockManager fileLockManager;
    private List<String> allDirs = new ArrayList<>();
    private CuratorFramework zkClient;
    private String zkAddress;

    private FileLockManager(String zkAddress){
        init();
        this.zkAddress = zkAddress;
        this.zkClient = connectZookeeper();
    }

    private void init(){
        String workDirPath = Config.WORKER_FILE_PATH;
        File workFile = new File(workDirPath);
        if(!workFile.isDirectory()){
            log.error("The path " + workDirPath + " is not exist.");
            System.exit(1);
        }
        File[] listFiles = workFile.listFiles();
        // 得到当前worker进程对应的目录
        File dirForThisWorker = null;
        if (listFiles != null && listFiles.length > 0) {
           for(File f : listFiles){
               if(f.isDirectory()){
                   allDirs.add(f.getName());
               }
           }
        }
    }

    private CuratorFramework connectZookeeper() {
        CuratorFramework zkClient;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(6000, 3);
        zkClient = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(25000)
                .connectionTimeoutMs(20000)
                .build();
        zkClient.start();
        log.info("Connect zookeeper successfull, zk address is " + zkAddress);
        return zkClient;
    }

    public static FileLockManager getInstance(String zkAddress){
        if(fileLockManager == null){
            fileLockManager = new FileLockManager(zkAddress);
        }
        return fileLockManager;
    }

    public String getDirNotLockd(){
        try {
            String dirNotLockd = null;
            List<String> pathes = null;
            try {
                pathes = zkClient.getChildren().forPath(Config.FILE_LOCK_PATH + "/" + Config.WORKER_ADDRESS);
            }catch (Exception e){
                zkClient.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(Config.FILE_LOCK_PATH + "/" + Config.WORKER_ADDRESS);
                pathes = zkClient.getChildren().forPath(Config.FILE_LOCK_PATH + "/" + Config.WORKER_ADDRESS);
            }
            log.info("Locked dirs are " + pathes.toString());
            log.info("The all dir are " + allDirs.toString());
            for(String dirPath : allDirs){
                if(!pathes.contains(dirPath)){
                    dirNotLockd = dirPath;
                }
            }
            if(dirNotLockd == null){
                log.error("All dir has locked");
                return null;
            }

            Map<String, String> param = new HashMap<>();
            param.put("workerId", Config.WORKER_ID);
            zkClient.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(Config.FILE_LOCK_PATH + "/" + Config.WORKER_ADDRESS+ "/" + dirNotLockd, GsonUtil.mapToJson(param).getBytes());
            return dirNotLockd;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage()); //System.exit(1);
            return null;
        }
    }
}
