package com.hzgc.compare.command;

import com.hzgc.common.rpc.client.RpcClient;
import com.hzgc.common.rpc.client.result.AllReturn;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.conf.Config;
import com.hzgc.compare.server.MasterServer;
//import org.apache.log4j.Logger;

import java.util.List;

public class RpcClientForMaster {
//    private static final Logger logger = LoggerFactory.getLogger(RpcClientForMaster.class);
//    private static Logger logger = Logger.getLogger(RpcClientForMaster.class);
    private MasterServer server;
    private void createService(String serverAddress){
        Constant constant = new Constant("/compare/master", "master");
        RpcClient rpcClient = new RpcClient(serverAddress, constant);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server = rpcClient.createAll(MasterServer.class);
    }

    private void submitJob(String workerId){
        server.submitJob(workerId);
    }

    private void getJobsNow(){
        AllReturn<List<String>> allReturn = server.getJobsNow();
        List<String> res = allReturn.getResult().get(0);
        for(String str : res){
            System.out.println(str);
        }
    }

    public static void main(String args[]) throws InterruptedException {
        RpcClientForMaster clientForMaster = new RpcClientForMaster();
        if(args.length == 2 && args[0].equals("--submit") && args[1].length() > 0){
            clientForMaster.createService(Config.ZK_ADDRESS);
            clientForMaster.submitJob(args[1]);
            Thread.sleep(2000);
            System.exit(0);
        } else if (args.length == 1 && args[0].equals("--getjobs")){
            clientForMaster.createService(Config.ZK_ADDRESS);
            clientForMaster.getJobsNow();
            Thread.sleep(2000);
            System.exit(0);
        } else if (args.length == 1 && args[0].equals("--help")){
            String info = "illegal argument: --help \n"
                    + "usage: \t admin-master.sh [--help] \n"
                    + "\t\t admin-master.sh [--submit] [workname] \n"
                    + "\t\t admin-master.sh [--getjobs]";
//            logger.info(info);
            System.out.println(info);
        } else{
            System.out.println("无效用法 --help");
//            logger.info("无效用法 --help");
        }
    }
}
