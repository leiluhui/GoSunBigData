package com.hzgc.cluster.peoman.jobclient;


import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.JobClientBuilder;
import com.github.ltsopensource.jobclient.domain.Response;
import com.hzgc.cluster.peoman.zk.Constant;
import com.hzgc.cluster.peoman.zk.JobDiscover;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JobClientMain {
    //第一次启动task
    public void getJobStart() throws InterruptedException {
        JobClient jobClient = new JobClientBuilder()
                .setPropertiesConfigure("application-pro.properties")
                .setJobCompletedHandler(new JobCompleteHandlerImpl())
                .build();

        jobClient.start();
        //获取参数list
        List<String> list = getParam();
        System.out.println(list);
        //提交多个task
        System.out.println("list的值为： " + list);
        if (list.size() > 0) {
            for (String i : list) {
                System.out.println("i的值为： " + i);
                submitJob(jobClient, i);
                Thread.sleep(5000);
                JobDiscover jobDiscover = new JobDiscover("172.18.18.100");
                boolean stat = jobDiscover.isExist(Constant.rootPath + "/" + i);
                if (stat){
                    System.out.println("gai ");
                }else {

                }
            }
        }
        //提交完成之后停止jobClient
        jobClient.stop();
    }

    //当有work掉线时，重启该work
    public void restartOneJob(String param) {
        JobClient jobClient = new JobClientBuilder()
                .setPropertiesConfigure("application-pro.properties")
                .setJobCompletedHandler(new JobCompleteHandlerImpl())
                .build();
        jobClient.start();
        submitJob(jobClient, param);
        jobClient.stop();
    }

    //提交任务
    private static void submitJob(JobClient jobClient, String param) {
        Job job = new Job();
        job.setTaskId("t_1");
        job.setParam("param", param);
        job.setTaskTrackerNodeGroup("test_trade_TaskTracker");      // 执行要执行该任务的taskTracker的节点组名称
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        Response response = jobClient.submitJob(job);
        System.out.println(response);
    }


    //获取准备的人口库条数
    public String getCount() {
        //连接数据库获取人口库总条数
        Connection connection;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://172.18.18.119:4000/people";
        String user = "root";
        String password = "";
        String count = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            if (!connection.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
                Statement statement = connection.createStatement();
                String sql = "select count(*) as count from t_people";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    count = resultSet.getString("count");
                    System.out.println("Count is : " + count);
                }
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    //连接数据库获取人口库条数，计算参数存入list中
    public List<String> getParam() {
        String count = getCount();
        //将条数进行等分
        List<String> list = new ArrayList<>();
        int countInt = Integer.parseInt(count);
        int capacity = 5;
        int num = countInt / capacity;
        int remaind = countInt % capacity;
        if (num == 0) {
            list.add("0_" + (capacity - 1));
        } else {
            list.add("0_" + (capacity - 1));
            if (remaind == 0) {
                for (int i = 1; i <= num; i++) {
                    list.add(i * capacity + "_" + ((i + 1) * capacity - 1));
                }
            } else {
                for (int i = 1; i <= num; i++) {
                    list.add(i * capacity + "_" + ((i + 1) * capacity - 1));
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        SpringApplication.run(JobClientMain.class, args);
    }
}
