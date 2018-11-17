package com.hzgc.compare.worker.persistence;

import com.hzgc.compare.worker.common.collects.CustomizeArrayList;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.util.KryoSerializer;
import com.hzgc.compare.worker.common.tuple.Pair;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class HDFSFileReader extends FileReader {
//    private static final Logger logger = LoggerFactory.getLogger(HDFSFileReader.class);
    private static Logger log = Logger.getLogger(HDFSFileReader.class);
    private HDFSStreamCache streamCache = HDFSStreamCache.getInstance();
    private int readFilesPerThread = 1;
    private List<ReadFileForHDFS> list = new ArrayList<>();


    public HDFSFileReader() {
        init();
    }

    public void init(){
        path = Config.WORKER_FILE_PATH;
        readFilesPerThread = Config.WORKER_READFILES_PER_THREAD;
        excutors = Config.WORKER_EXECUTORS_TO_LOADFILE;
        pool = Executors.newFixedThreadPool(excutors);
    }

    @Override
    public void loadRecord() throws IOException {
        FileSystem fileSystem = streamCache.getFileSystem();
        String workId = Config.WORKER_ID;
        Path workDir = new Path(path);
        if(!fileSystem.isDirectory(workDir)){
            return;
        }
        Path dirForThisWorker = new Path(path, workId);
        if(!fileSystem.isDirectory(dirForThisWorker)){
            return;
        }

        //得到本月和上月
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String ym = sdf.format(date);
        String [] strings = ym.split("-");
        Integer m = Integer.valueOf(strings[1]) - 1;
        String lastMonth = null;
        if (m > 0 && m < 10){
            lastMonth = strings[0] + "-0" + m;
        }
        if (m == 0) {
            int year = Integer.valueOf(strings[0]) - 1;
            lastMonth = String.valueOf(year) + "-" + String.valueOf(12);
        }
        long start = System.currentTimeMillis();
        // 加载上月的记录
        loadRecordForMonth2(dirForThisWorker, lastMonth);
        // 加载本月的记录
        loadRecordForMonth2(dirForThisWorker, ym);

        if(list.size() == 0){
            log.info("There is no file to load.");
            return;
        }
        for(ReadFileForHDFS readFile1: list){
            pool.submit(readFile1);
//            readFile1.start();
        }

        while (true){
            boolean flug = true;
            for(ReadFileForHDFS readFile1: list){
                flug = readFile1.isEnd() && flug;
            }
            if(flug){
                break;
            }
        }
        pool.shutdown();
        log.info("The time used to load record is : " + (System.currentTimeMillis() - start));
    }

    /**
     * 加载一个月的数据到内存（内存存储byte特征值）
     * @param path 目录
     * @param month 目标月份
     */
    private void loadRecordForMonth2(Path path, String month) throws IOException {
        FileSystem fileSystem = streamCache.getFileSystem();
        log.info("Read month is : " + month);
        //得到目标月份的文件夹
        Path monthDir = new Path(path.toString(), month);
        if(!fileSystem.isDirectory(monthDir)){
            return;
        }

        FileStatus[] files = fileSystem.listStatus(monthDir);
        if(files.length == 0){
            return;
        }
        //多线程加载数据文件
        ReadFileForHDFS readFile = new ReadFileForHDFS();
        list.add(readFile);
        int index = 0;
        for(FileStatus file: files){
            if(index < readFilesPerThread){
                readFile.addFile(file);
                index ++;
            }else{
                readFile = new ReadFileForHDFS();
                list.add(readFile);
                readFile.addFile(file);
                index = 0;
            }
        }
    }
}

class ReadFileForHDFS implements Runnable{
//    private static final Logger logger = LoggerFactory.getLogger(ReadFileForHDFS.class);
    private static Logger log = Logger.getLogger(ReadFileForHDFS.class);
    private MemoryCacheImpl memoryCacheImpl1 = MemoryCacheImpl.getInstance();
    private HDFSStreamCache streamCache = HDFSStreamCache.getInstance();
    private boolean end = false;
    private List<FileStatus> list = new ArrayList<>();
    private FileSystem fileSystem = streamCache.getFileSystem();
    private KryoSerializer kryoSerializer = new KryoSerializer(RecordToFlush.class);

    void addFile(FileStatus file){
        list.add(file);
    }

    boolean isEnd(){
        return end;
    }

    //解析数据，存入temp
//    private void addRecordToMap2(byte[] record, Map<Triplet<String, String, String>, List <Pair<String, float[]>>> temp){
//        RecordToFlush recordToFlush = kryoSerializer.deserialize(record);
//        Triplet <String, String, String> key = new Triplet <>(recordToFlush.getFirst(), null, recordToFlush.getSecond());
//        float[] floats = recordToFlush.getFourth();
//        Pair<String, float[]> value = new Pair <>(recordToFlush.getThird(), floats);
//        List<Pair<String, float[]>> list = temp.computeIfAbsent(key, k -> new CustomizeArrayList<>());
//        list.add(value);
//    }

    //解析数据，存入temp
    private void addRecordToMap(byte[] record, Map<String, List <Pair<String, byte[]>>> temp) throws IOException {
        RecordToFlush recordToFlush = kryoSerializer.deserialize(record);
        String key = recordToFlush.getFirst();
        byte[] bytes = recordToFlush.getThird();
        Pair<String, byte[]> value = new Pair <>(recordToFlush.getSecond(), bytes);
        List<Pair<String, byte[]>> list = temp.computeIfAbsent(key, k -> new CustomizeArrayList<>());
        list.add(value);
    }

    @Override
    public void run() {
        long count = 0L;
        Map<String, List <Pair<String, byte[]>>> temp = new HashMap<>();
        for(FileStatus f : list){
            try {
                if(!fileSystem.isFile(f.getPath())){
                    return;
                }
                log.info("Read file : " + f.getPath().toString());
                FSDataInputStream inputStream = streamCache.getReaderStream(f.getPath().toString());
                while (true){
                    int len;
                    try {
                        len = inputStream.readInt();
                    } catch(java.io.EOFException e){
                        break;
                    }
                    if(len == 0){
                        break;
                    }
                    byte[] buffer = new byte[len];
                    try {
                        inputStream.readFully(inputStream.getPos(), buffer);
                    } catch (java.io.EOFException e){
                        break;
                    }
                    long res = inputStream.skip(len);
                    if(res == 0){
                        break;
                    }
//                    String[] s = new String(buffer).trim().split("_");
                    addRecordToMap(buffer, temp);
                    count ++;
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("The num of Records Loaded is : " + count);
        memoryCacheImpl1.loadCacheRecords(temp);
        end = true;
    }
}