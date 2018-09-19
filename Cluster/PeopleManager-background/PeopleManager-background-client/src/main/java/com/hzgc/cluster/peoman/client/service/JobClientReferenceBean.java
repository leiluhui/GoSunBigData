package com.hzgc.cluster.peoman.client.service;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.hzgc.cluster.peoman.client.zookeeper.Constant;
import com.hzgc.cluster.peoman.client.zookeeper.JobDiscover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JobClientReferenceBean implements InitializingBean {
    @Autowired
    private JobClient jobClient;
    @Autowired
    private JobDiscover jobDiscover;
    @Autowired
    private JobGetMap jobGetMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Start to listen the nodes!");
    }

    public void startJob(Map<String, String> map) {
        for (String key : map.keySet()) {
            String value = map.get(key);
            String offset = value.substring(0,value.indexOf("-"));
            String max = value.substring(value.indexOf("-")+1);
            String limit = String.valueOf(Integer.parseInt(max) - Integer.parseInt(offset) + 1);
            Job job = new Job();
            job.setTaskId("hzgc");
            job.setParam("offset", value);
            job.setParam("limit",limit);
            job.setParam("run","");
            job.setTaskTrackerNodeGroup(key);
            job.setNeedFeedback(true);
            job.setReplaceOnExist(true);
            Response response = jobClient.submitJob(job);
            log.info("The response is : " + response);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jobClient.stop();
        }
    }

    public void stopWorker(){
       List<String> childPath = jobDiscover.listGroup(Constant.rootPath);
       for (String child : childPath){
           Job job = new Job();
           job.setTaskId("hzgc");
           job.setParam("stop","");
           job.setTaskTrackerNodeGroup(child);
           job.setNeedFeedback(true);
           job.setReplaceOnExist(true);
           Response response = jobClient.submitJob(job);
           log.info("The response is : " + response);
           try {
               Thread.sleep(10000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
        jobClient.stop();
    }

    public Map<String,String> restartJob(){
        List<String> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        List<String> nodeList = jobDiscover.listGroup(Constant.rootPath);
        int count = Integer.parseInt(jobGetMap.getCount());
        int capacity = nodeList.size();
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
        for (int i = 0; i < nodeList.size(); i++){
            String childGroup = nodeList.get(i).substring(nodeList.indexOf("an/")+3);
            map.put(childGroup,list.get(i));
        }
        return map;
    }
}
