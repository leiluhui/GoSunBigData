package com.hzgc.cluster.peoman.jobclient;

import com.hzgc.cluster.peoman.zk.DiscoveCallBack;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JobClientCallBack implements DiscoveCallBack {
    private Logger LOG = Logger.getLogger(JobClientCallBack.class);

    @Override
    public void run(List<ChildData> currentData, PathChildrenCacheEvent event) throws InterruptedException {
        JobClientMain jobClientMain = new JobClientMain();
        if (event == null) {
            LOG.info("第一次启动客户端：");
            jobClientMain.getJobStart();
        } else {
            if (event.getType().toString().equals("CHILD_ADDED")) {
                LOG.info("当前节点变动类型为： " + event.getType() + "不做处理！！！");
            } else if (event.getType().toString().equals("CHILD_REMOVED")) {
                List<String> list = jobClientMain.getParam();
                List<String> childList = new ArrayList<>();
                for (ChildData childData : currentData) {
                    String childParam = childData.getPath().substring(childData.getPath().indexOf("er/") + 3);
                    childList.add(childParam);
                }
                LOG.info("从数据库中获取得到的参数为： " + list);
                LOG.info("从zookeeper中获取得到的参数为： " + childList);
                for (String child : childList) {
                    if (list.contains(child)) {
                        System.out.println(child);
                        list.remove(child);
                    }
                }
                LOG.info("删除之后list的值为： " + list);
                for (String param : list) {
                    jobClientMain.restartOneJob(param);
                }
            } else if (event.getType().toString().equals("CHILD_UPDATED")) {
                //从数据库中读出所有的数据
                List<String> list = jobClientMain.getParam();
                List<String> childList = new ArrayList<>();
                //zookeeper中节点全都读出来存入list中
                for (ChildData childData : currentData) {
                    String childParam = childData.getPath().substring(childData.getPath().indexOf("er/") + 3);
                    childList.add(childParam);
                }
                //删除已经存在的参数
                for (String child : childList) {
                    if (list.contains(child)) {
                        list.remove(child);
                    }
                }
                //如果list中还存在参数，则将参数传给worker启动
                if (list.size() > 0) {
                    for (String param : list) {
                        jobClientMain.restartOneJob(param);
                    }
                }
            }
        }
    }
}
