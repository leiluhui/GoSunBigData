package com.hzgc.compare.worker;

import com.hzgc.compare.worker.common.taskhandle.TaskToHandleQueue;
import com.hzgc.compare.worker.comsumer.Comsumer;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.memory.manager.MemoryManager;
import com.hzgc.compare.worker.persistence.*;
import com.hzgc.jniface.FaceFunction;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * 整合所有组件
 */
public class FaceCompareWorker {
//    private static final Logger logger = LoggerFactory.getLogger(FaceCompareWorker.class);
    private static Logger log = Logger.getLogger(FaceCompareWorker.class);
    private Comsumer comsumer;
    private MemoryManager memoryManager;
    private FileManager fileManager;
//    private HBaseClient hBaseClient;


    public void init(String workerId, String port){
        Config.WORKER_ID = workerId;
        Config.WORKER_RPC_PORT =  Integer.parseInt(port);
        comsumer = new Comsumer();
        log.info("To start worker " + workerId);
        log.info("To init the memory module.");
        MemoryCacheImpl.getInstance();
        memoryManager = new MemoryManager();
        log.info("To init persistence module.");
        int saveParam = Config.WORKER_FILE_SAVE_SYSTEM;
        if(Config.SAVE_TO_LOCAL == saveParam){
            fileManager = new LocalFileManager();
        } else if(Config.SAVE_TO_HDFS == saveParam){
            fileManager = new HDFSFileManager();
        }
//        hBaseClient = new HBaseClient();
        try {
            log.info("Load data from file System.");
            if(Config.SAVE_TO_LOCAL == saveParam){
                FileReader fileReader = new LocalFileReader();
                fileReader.loadRecord();
            } else if(Config.SAVE_TO_HDFS == saveParam){
                FileReader fileReader = new HDFSFileReader();
                fileReader.loadRecord();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TaskToHandleQueue.getTaskQueue();
    }


    public void start(){
        long start = System.currentTimeMillis();
        log.info("ES Param : className " + Config.ES_CLUSTER_NAME + ", ESHost " + Config.ES_HOST + ", ESPort " + Config.ES_CLUSTER_PORT);
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ElasticSearchClient.connect();
        log.info("The Time connect to ES is " + (System.currentTimeMillis() - start));
        comsumer.start();
        memoryManager.startToCheck();
        memoryManager.toShowMemory();
        if(Config.WORKER_FLUSH_PROGRAM == 0){
            memoryManager.timeToCheckFlush();
        }
        // FIXME: 18-9-21 
        fileManager.checkTaskTodo();
//        hBaseClient.timeToWrite();
        FaceFunction.init();
    }

    public static void main(String args[]){
        if(args.length != 4){
            return;
        }

        String workerId = args[0];
        String nodeGroup = args[1];
        String port = args[2];
        String taskId = args[3];
        FaceCompareWorker worker = new FaceCompareWorker();
        worker.init(workerId, port);
        RPCRegistry rpcRegistry = new RPCRegistry(workerId, nodeGroup, port, taskId);
        Thread thread = new Thread(rpcRegistry);
        thread.start();

        int count = 0;
        while(!rpcRegistry.checkJob()){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            count ++;
            if(count > 12){
                log.error("Registry to Zookeeper faild.");
                System.exit(1);
            }
        }
        worker.start();
//        Thread thread = new Thread(new ZookeeperRegistry(workerId, nodeGroup, port, taskId));
//        thread.start();
    }
}
