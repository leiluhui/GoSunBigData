package com.hzgc.compare.submit;


import com.github.ltsopensource.jobclient.JobClient;
import com.hzgc.compare.conf.Config;
import org.apache.log4j.Logger;


public class JobClientUtil {
//    private static final Logger logger = LoggerFactory.getLogger(JobClientUtil.class);
    private static Logger logger = Logger.getLogger(JobClientUtil.class);
    private static JobClient client;

    public static JobClient getClient(){
        if(client == null){
            client = createClient();
        }
        return client;
    }

    private  static JobClient createClient(){
        logger.info("Create a Job Client.");
        JobClient client = new JobClient();
        client.addConfig("zk.client", "zkclient");
        client.addConfig("lts.remoting", "netty");
        client.addConfig("lts.json", "jackson");
        client.addConfig("job.fail.store", "mapdb");
        client.setNodeGroup("compare_master");
        client.setClusterName(Config.CLUSTER_NAME);
        client.setRegistryAddress("zookeeper://" + Config.ZK_ADDRESS);
        client.start();
        return client;
    }
}
