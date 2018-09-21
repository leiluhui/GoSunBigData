package com.hzgc.cluster.peoman.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Component
public class StartWorkers {
    public void startWorkers(){
        String bashCommand = "sh /opt/GoSunBigData/Cluster/peoman/bin/start-workers.sh";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(bashCommand);
            int status = process.waitFor();
            if (status != 0) {
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
    }
}
