package com.hzgc.cluster.peoman.tasktracker;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyJobRunner implements JobRunner {
    private static final Logger LOGGER = Logger.getLogger(MyJobRunner.class);
    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            // 业务逻辑
            LOGGER.info("我要执行：" + jobContext);
            String param = jobContext.getJob().getParam("param");
            try {
                LOGGER.info("开始正常执行，参数为：" + param);
                String bashCommand = "sh /opt/jj.sh " + param;
                System.out.println("脚本运行指令为： " + bashCommand);
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(bashCommand);
                int status = process.waitFor();
                if (status != 0 ){
                    System.out.println("Failed to call shell's command!");
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuffer strbr = new StringBuffer();
                String line;
                while ((line = br.readLine())!= null)
                {
                    strbr.append(line).append("\n");
                }
                String result = strbr.toString();
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");

    }
}