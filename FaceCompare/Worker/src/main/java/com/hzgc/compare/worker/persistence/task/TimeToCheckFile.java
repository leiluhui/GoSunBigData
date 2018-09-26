package com.hzgc.compare.worker.persistence.task;

import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.persistence.HDFSStreamCache;
import com.hzgc.compare.worker.util.DateUtil;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

/**
 * 定期任务，检查文件是否存在过期，并删除过期文件,按照月份进行删除
 */

public class TimeToCheckFile extends TimerTask {
    private static Logger log = Logger.getLogger(TimeToCheckFile.class);
    private String path;
    private int tag;  //1标识删除文件
    private String workId;
//    private static Logger LOG = Logger.getLogger(TimeToCheckFile.class);

    public TimeToCheckFile() {
        init();
    }

    public void init() {
        path = Config.WORKER_FILE_PATH;
        tag = Config.DELETE_OPEN;
        workId = Config.WORKER_ID;
    }

    private void deleteFilesOnHDFS(){
        HDFSStreamCache streamCache = HDFSStreamCache.getInstance();
        FileSystem fileSystem = streamCache.getFileSystem();
        try {
            Path rootDir =  new Path(path);
            if(!fileSystem.isDirectory(rootDir)){
                return;
            }

            Path workDir = new Path(path, workId);
            if(!fileSystem.isDirectory(workDir)){
                return;
            }
            FileStatus[] fileStatuses = fileSystem.listStatus(workDir);
            if(fileStatuses.length == 0){
                return;
            }

            List<String> monthes = DateUtil.getMonthes();
            for(FileStatus fileStatus : fileStatuses){
                String month = fileStatus.getPath().getName();
                if(!month.equals(monthes.get(0)) || !month.equals(monthes.get(1))){
                    fileSystem.delete(fileStatus.getPath(), true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFilesOnLocal(){
        File rootDir = new File(path);
        if(!rootDir.isDirectory()){
            return;
        }
        File workDir = new File(path, workId);
        if(!workDir.isDirectory()){
            return;
        }
        File[] files = workDir.listFiles();
        if(files == null || files.length == 0){
            return;
        }
        List<String> monthes = DateUtil.getMonthes();
        for(File file : files){
            String month = file.getName();
            if(!month.equals(monthes.get(0)) || !month.equals(monthes.get(1))){
                deleteFolder(file);
            }
        }
    }

    private void deleteFolder(File file){
        if(!file.exists()){
            return;
        }
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null || files.length == 0){
                file.delete();
            } else{
                deleteFolder(file);
            }
        }
    }

    @Override
    public void run() {
        if (1 == tag) {
            int saveParam = Config.WORKER_FILE_SAVE_SYSTEM;
            if(saveParam == 0){
                deleteFilesOnLocal();
            } else if(saveParam == 1){
                deleteFilesOnHDFS();
            }
        } else {
            log.info("Data is not delete");
        }
    }
}
