package com.hzgc.cluster.peoman.client.service;

import com.hzgc.cluster.peoman.client.zookeeper.Constant;
import com.hzgc.cluster.peoman.client.zookeeper.DiscoveCallBack;
import com.hzgc.cluster.peoman.client.zookeeper.JobDiscover;
import com.hzgc.cluster.peoman.client.zookeeper.JobRegister;
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
    @Value("${lts.tasktracker.nodes}")
    private String nodes;

    @Autowired
    @Value("${zookeeper.connection.address}")
    private String zkAddress;

    @Autowired
    @Value("${every.point.num}")
    private String number;

    @Override
    public void run(List<ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException {
        if (event == null) {
            List<String> childList = new ArrayList<>();
            for (ChildData childData : currentData) {
                String childParam = childData.getPath().substring(childData.getPath().indexOf("de/") + 3);
                childList.add(childParam);
            }
            log.info("启动客户端,检查到worker的节点为： " + childList);
            String[] str = nodes.split(",");
            List<String> nodestr = new ArrayList<>();
            Collections.addAll(nodestr, str);
            log.info("规定的worker的节点为 ： " + nodestr);
            //当从zookeeper中读取的worker和规定的worker一样时
            if (childList.size() == nodestr.size() && nodestr.containsAll(childList)){
                List<String> list = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                if (currentData.size() == 0) {
                    log.info("This node not have point,please check the task tracker is running!!!");
                } else {
                    for (int i=0; i< currentData.size();i++){
                        String childGroup = currentData.get(i).getPath().substring(currentData.get(i).getPath().indexOf("de/") + 3);
                        map.put(childGroup,i*Integer.parseInt(number)+"-"+number);
                    }
                }
                log.info("分割好的map为： " + map);
                try {
                    JobRegister jobRegister = new JobRegister(zkAddress);
                    for (String key : map.keySet()) {
                        jobRegister.regist(key,map.get(key));
                    }
                    jobClientReferenceBean.startJob(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                log.info("Please check the all workers is running!!");
                System.exit(0);
            }
        } else {
            Map<String,String> map = new HashMap<>();
            if (event.getType().toString().equals("CHILD_REMOVED")){
                JobDiscover jobDiscover = new JobDiscover(zkAddress);
                List<String> tempList = jobDiscover.listGroup(Constant.tempPath);
                List<String> paristList = jobDiscover.listGroup(Constant.rootPath);
                for (String temp : tempList){
                    if (paristList.contains(temp)){
                        paristList.remove(temp);
                    }
                }
                if (paristList.size() > 0){
                    for (String parist : paristList){
                        String date = jobDiscover.getData(Constant.rootPath+ "/" + parist);
                        map.put(parist,date);
                        jobClientReferenceBean.startJob(map);
                    }
                }
            }
        }
    }
}
