package com.hzgc.cluster.peoman.client.service;

import com.hzgc.cluster.peoman.client.zookeeper.DiscoveCallBack;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class JobClientCallBack implements DiscoveCallBack {
    @Autowired
    private JobGetMap jobGetCount;
    @Autowired
    private JobClientReferenceBean jobClientReferenceBean;
    @Autowired
    private StartWorkers startWorkers;
    @Autowired
    private StopWorkers stopWorkers;
    @Autowired
    @Value("${lts.tasktracker.nodes}")
    private String nodes;

    @Override
    public void run(List<ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException {
        if (event == null) {
            List<String> childList = new ArrayList<>();
            for (ChildData childData : currentData) {
                String childParam = childData.getPath().substring(childData.getPath().indexOf("an/") + 3);
                childList.add(childParam);
            }
            log.info("启动客户端,检查到worker的节点为： " + childList);
            String[] str = nodes.split(",");
            List<String> nodestr = new ArrayList<>();
            Collections.addAll(nodestr, str);
            for (String child : childList){
                if (nodestr.contains(child)){
                    childList.remove(child);
                }
            }
            if (childList.size() == 0){
                List<String> list = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                if (currentData.size() == 0) {
                    log.info("This node not have point,please check the tasktracker is running!!!");
                } else {
                    int count = Integer.parseInt(jobGetCount.getCount());
                    int capacity = currentData.size();
                    int num = count / capacity;
                    for (int i = 0; i < capacity; i++) {
                        if (i == 0) {
                            list.add("0-" + ((i + 1) * num - 1));
                        } else if (i == (capacity - 1)) {
                            list.add("" + i * num + "-" + count);
                        } else {
                            list.add("" + i * num + "-" + ((i + 1) * num - 1));
                        }
                    }
                    for (int i = 0; i < currentData.size(); i++) {
                        String childGroup = currentData.get(i).getPath().substring(currentData.get(i).getPath().indexOf("de/") + 3);
                        map.put(childGroup, list.get(i));
                    }
                }
                try {
                    jobClientReferenceBean.startJob(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                log.info("Please check the all workers is running!!");
                System.exit(0);
            }
        } else {
            if (event.getType().toString().equals("CHILD_ADDED")) {
                try {
                    jobClientReferenceBean.stopWorker();
                    Thread.sleep(5000);
                    jobClientReferenceBean.startJob(jobClientReferenceBean.restartJob());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event.getType().toString().equals("CHILD_REMOVED")) {
                try {
                    jobClientReferenceBean.stopWorker();
                    Thread.sleep(5000);
                    jobClientReferenceBean.startJob(jobClientReferenceBean.restartJob());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
