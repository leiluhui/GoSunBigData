package com.hzgc.compare.worker.persistence;

import com.hzgc.compare.worker.common.tuple.Triplet;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.persistence.task.TimeToCheckFile;
import com.hzgc.compare.worker.persistence.task.TimeToCheckTask;
import com.hzgc.compare.worker.util.KryoSerializer;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class HDFSFileManager implements FileManager {
    private String path = ""; //文件保存目录
    private Long fileSize = 64L * 1024 * 1024L;
    private Long timeToCheckFile = 24 * 60 * 60 * 1000L;
    private String work_id = "";
    private static Logger LOG = Logger.getLogger(HDFSFileManager.class);
    private HDFSStreamCache streamCache;
    private KryoSerializer kryoSerializer;

    public HDFSFileManager(){
        init();
    }

    public void init() {
        path = Config.WORKER_FILE_PATH;
        work_id = Config.WORKER_ID;
        timeToCheckFile = Config.WORKER_FILE_CHECK_TIME;
        fileSize = Config.WORKER_FILE_SIZE;
        streamCache = HDFSStreamCache.getInstance();
        kryoSerializer = new KryoSerializer(RecordToFlush.class);
    }

    public void flush() {

    }


    public void flush(List<Triplet<String, String, byte[]>> buffer) {
        LOG.info("Flush records to HDFS, the num is " + buffer.size());
        Map<String , List<RecordToFlush>> temp = new Hashtable<>();
        for(Triplet<String, String, byte[]> quintuple : buffer){
            byte[] feature = quintuple.getThird();
            String dateYMD = quintuple.getFirst();
            String[] strings = dateYMD.split("-");
            String dateYM = strings[0] + "-" + strings[1];
            List<RecordToFlush> list = temp.computeIfAbsent(dateYM, k -> new ArrayList<>());
            list.add(new RecordToFlush(dateYMD, quintuple.getSecond(), feature));
        }

        for(Map.Entry<String , List<RecordToFlush>> entry : temp.entrySet()){
            String dateYM = entry.getKey();
            List<RecordToFlush> datas = entry.getValue();
            try {
                flushForMonth(dateYM, datas);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void flushForMonth(String month, List<RecordToFlush> datas) throws IOException{
        FileSystem fileSystem = streamCache.getFileSystem();
        if (!fileSystem.exists(new Path(path))) {
            boolean res = fileSystem.mkdirs(new Path(path));
            if(!res){
                throw new IOException("创建文件夹 " + path + " 失败");
            }
        }
        //创建workid目录
        Path workFile = new Path(path, work_id);
        if (!fileSystem.exists(workFile)) {
            boolean res = fileSystem.mkdirs(workFile);
            if(!res){
                throw new IOException("创建文件夹 " + workFile.toString() + " 失败");
            }
            LOG.info("WorkFile name is " + workFile.getName());
        }
        //寻找目标月的文件夹
//        File dirMonth = new File(workFile, month);
        Path dirMonth = new Path(workFile.toString(), month);
        //若没有该文件夹，创建
        if(!fileSystem.exists(dirMonth) || !fileSystem.isDirectory(dirMonth)){
            boolean res = fileSystem.mkdirs(dirMonth);
            if(!res){
                throw new IOException("创建文件夹 " + dirMonth.toString() + " 失败");
            }
        }
        //寻找目标文件
        FileStatus[] files = fileSystem.listStatus(dirMonth);
        Path fileToWrite;
        Comparator<FileStatus> comparator = (o1, o2) -> {
            int f1 = Integer.parseInt(o1.getPath().getName().split("\\.")[0]);
            int f2 = Integer.parseInt(o2.getPath().getName().split("\\.")[0]);
            return f1 - f2;
        };

        long theFileSize = 0L;
        if(files == null || files.length == 0){
            fileToWrite = new Path(dirMonth, 0 + ".txt");
        } else{
            FileStatus fileStatus = files[0];
            for(FileStatus f : files){
                if(comparator.compare(f, fileStatus) > 0){
                    fileStatus = f;
                }
            }
            fileToWrite = fileStatus.getPath();
            theFileSize = streamCache.getLen(dirMonth);
        }


//        long num = 1L;
//        long startCount = theFileSize;
        for(RecordToFlush data : datas) {
            FSDataOutputStream os;
            byte[] bytes = new byte[124];
            kryoSerializer.serialize(data, bytes);
            Integer fileName = Integer.valueOf(fileToWrite.getName().split("\\.")[0]);
            //判断文件大小
            if (theFileSize + bytes.length + 4 >= fileSize) {
                os = streamCache.getStream(dirMonth, fileToWrite);
                os.hflush();
                os.close();
                fileName = fileName + 1;
                fileToWrite = new Path(dirMonth, String.valueOf(fileName) + ".txt");
//                num = 1;
            }

            os = streamCache.getStream(dirMonth, fileToWrite);
            os.writeInt(bytes.length);
            os.write(bytes);

            streamCache.addLen(dirMonth, bytes.length + 4L);
            theFileSize = streamCache.getLen(dirMonth);
//            if(streamCache.getLen(dirMonth) - startCount >= 1024 * 64 * num) {
//                num ++;
//                os.hflush();
//                LOG.info("HFLUSH ! the File Size1 : " + fileSystem.getFileStatus(fileToWrite).getLen());
//                LOG.info("HFLUSH ! the File Size2 : " + streamCache.getLen(dirMonth));
//            }
        }
        streamCache.getStream(dirMonth, fileToWrite).hflush();
    }

    public void checkFile() {
        new Timer().schedule(new TimeToCheckFile(), timeToCheckFile, timeToCheckFile);
    }

    public void checkTaskTodo() {
        new Timer().schedule(new TimeToCheckTask(this), Config.WORKER_CHECK_TASK_TIME,
                Config.WORKER_CHECK_TASK_TIME);
    }
}
