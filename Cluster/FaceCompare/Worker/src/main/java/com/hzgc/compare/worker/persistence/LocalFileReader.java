package com.hzgc.compare.worker.persistence;

import com.hzgc.compare.worker.common.collects.CustomizeArrayList;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;

public class LocalFileReader extends FileReader {
//    private static final Logger logger = LoggerFactory.getLogger(LocalFileReader.class);
    private static Logger log = Logger.getLogger(LocalFileReader.class);
    private int readFilesPerThread = 2;
    private List<ReadFileForLocal> list = new ArrayList<>();


    public LocalFileReader() {
        init();
    }

    public void init(){
        path = Config.WORKER_FILE_PATH;
        readFilesPerThread = Config.WORKER_READFILES_PER_THREAD;
        excutors = Config.WORKER_EXECUTORS_TO_LOADFILE;
        pool = Executors.newFixedThreadPool(excutors);
    }

    public void loadRecord() {
        String workId = Config.WORKER_ID;
        File workFile = new File(path);
        if(!workFile.isDirectory()){
            return;
        }
        File[] listFiles = workFile.listFiles();
        // 得到当前worker的目录
        File dirForThisWorker = null;
        if (listFiles != null && listFiles.length > 0) {
            for(File fi : listFiles){
                if(fi.isDirectory() && workId.equals(fi.getName())){
                    dirForThisWorker = fi;
                }
            }
        }

        if(dirForThisWorker == null || !dirForThisWorker.isDirectory()){
            return;
        }

        //得到本月和上月
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//        String ym = sdf.format(date);
//        String [] strings = ym.split("-");
//        Integer m = Integer.valueOf(strings[1]) - 1;
//        String lastMonth = null;
//        if (m > 0 && m < 10){
//            lastMonth = strings[0] + "-0" + m;
//        }
//        if (m == 0) {
//            int year = Integer.valueOf(strings[0]) - 1;
//            lastMonth = String.valueOf(year) + "-" + String.valueOf(12);
//        }

        // 得到所有月份目录
        File[] files = dirForThisWorker.listFiles();
        if(files == null || files.length == 0){
            return;
        }
        List<String> months = new ArrayList<>();
        for(File file : files){
            String name = file.getName();
            if(file.isDirectory() && isMonth(name)){
                months.add(name);
            }
        }
        long start = System.currentTimeMillis();
        // 加载记录
        for(String month : months){
            loadRecordForMonth(dirForThisWorker, month);
        }
        // 加载上月的记录
//        loadRecordForMonth(dirForThisWorker, lastMonth);
        // 加载本月的记录
//        loadRecordForMonth(dirForThisWorker, ym);

        if(list.size() == 0){
            log.info("There is no file to load.");
            return;
        }
        for(ReadFileForLocal readFile1: list){
            pool.submit(readFile1);
//            readFile1.start();
        }

        while (true){
            boolean flug = true;
            for(ReadFileForLocal readFile1: list){
                flug = readFile1.isEnd() && flug;
            }
            if(flug){
                break;
            }
        }
        pool.shutdown();
        log.info("The time used to load record is : " + (System.currentTimeMillis() - start));
    }

//    /**
//     * 加载一个月的数据到内存（内存存储byte特征值）
//     * @param fi 目录
//     * @param month 目标月份
//     */
//    private void loadRecordForMonth(File fi, String month){
//        logger.info("Read month is : " + month);
//        MemoryCacheImpl<String, String, byte[]> memoryCacheImpl1 = MemoryCacheImpl.getInstance();
//        //得到目标月份的文件夹
//        File monthdir = null;
//        File[] files = fi.listFiles();
//        if(files != null && files.length > 0){
//            for(File file : files){
//                if (file.isDirectory() && file.getName().equals(month)){
//                    monthdir = file;
//                }
//            }
//        }
//        if(monthdir == null){
//            return;
//        }
//        //遍历加载数据文件
//        File[] files1 = monthdir.listFiles();
//        if(files1 == null || files1.length == 0){
//            return;
//        }
//        long count = 0L;
//        Map<Triplet <String, String, String>, List <Pair <String, byte[]>>> temp = memoryCacheImpl1.getCacheRecords();
//
//        for(File f : files1){
//            if(f.isFile()){
//                logger.info("Read file : " + f.getAbsolutePath());
//                BufferedReader bufferedReader = streamCache.getReaderStream(f);
//                try {
//                    String line;
//                    //数据封装
//                    while ((line = bufferedReader.readLine()) != null) {
//                        String[] s = line.split("_");
//                        Triplet<String, String, String> key = new Triplet <>(s[0], null, s[1]);
//                        byte[] bytes = decoder.decodeBuffer(s[3]);
//                        Pair<String, byte[]> value = new Pair <>(s[2], bytes);
//                        List<Pair<String, byte[]>> list = temp.computeIfAbsent(key, k -> new CustomizeArrayList<>());
//                        list.add(value);
//                        count ++ ;
//                    }
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        logger.info("The num of Records Loaded is : " + count);
////        memoryCacheImpl1.loadCacheRecords(temp);
//    }

    /**
     * 加载一个月的数据到内存
     * @param fi 目录
     * @param month 目标月份
     */
    private void loadRecordForMonth(File fi, String month){
        log.info("Read month is : " + month);
        //得到目标月份的文件夹
        File monthdir = null;
        File[] files = fi.listFiles();
        if(files != null && files.length > 0){
            for(File file : files){
                if (file.isDirectory() && file.getName().equals(month)){
                    monthdir = file;
                }
            }
        }
        if(monthdir == null || !monthdir.isDirectory()){
            return;
        }
        //遍历加载数据文件
        File[] files1 = monthdir.listFiles();
        if(files1 == null || files1.length == 0){
            return;
        }
        ReadFileForLocal readFile = new ReadFileForLocal();
        list.add(readFile);
        int index = 0;
        for(File file: files1){
            if(index < readFilesPerThread){
                readFile.addFile(file);
                index ++;
            }else{
                readFile = new ReadFileForLocal();
                list.add(readFile);
                readFile.addFile(file);
                index = 0;
            }
        }
    }

    private boolean isMonth(String month){
        if(month == null || month.length() != 7){
            return false;
        }
        String[] sps = month.split("-");
        if(sps.length != 2){
            return false;
        }
        try {
            int mon = Integer.parseInt(sps[1]);
            return mon > 0 && mon <= 30;
        } catch (Exception e){
            return false;
        }

    }
}

class ReadFileForLocal implements Runnable{
//    private static final Logger logger = LoggerFactory.getLogger(ReadFileForLocal.class);
    private static Logger log = Logger.getLogger(ReadFileForLocal.class);
    private MemoryCacheImpl memoryCacheImpl1 = MemoryCacheImpl.getInstance();
    private LocalStreamCache streamCache = LocalStreamCache.getInstance();
    private boolean end = false;
    private List<File> list = new ArrayList<>();

    void addFile(File file){
        list.add(file);
    }

    boolean isEnd(){
        return end;
    }

    //解析数据，存入temp
//    private void addRecordToMap2(String[] record, Map<Triplet<String, String, String>, List <Pair<String, float[]>>> temp){
//        Triplet <String, String, String> key = new Triplet <>(record[0], null, record[1]);
//        float[] floats = FaceObjectUtil.jsonToArray(record[3]);
//        Pair<String, float[]> value = new Pair <>(record[2], floats);
//        List<Pair<String, float[]>> list = temp.computeIfAbsent(key, k -> new CustomizeArrayList<>());
//        list.add(value);
//    }

    //解析数据，存入temp
    private void addRecordToMap(String[] record, Map<String, List <Pair<String, byte[]>>> temp) throws IOException {
        String key = record[0];
        byte[] bytes = Base64.getDecoder().decode(record[2]);
        Pair<String, byte[]> value = new Pair <>(record[1], bytes);
        List<Pair<String, byte[]>> list = temp.computeIfAbsent(key, k -> new CustomizeArrayList<>());
        list.add(value);
    }

    @Override
    public void run(){
        long count = 0L;
        Map<String, List <Pair <String, byte[]>>> temp = new HashMap<>();
        for(File f : list){
            if(f.isFile()){
                log.info("Read file : " + f.getAbsolutePath());
                BufferedReader bufferedReader = streamCache.getReaderStream(f);
                try {
                    String line;
                    //数据封装
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] s = line.split("_");
                        addRecordToMap(s, temp);
                        count ++ ;
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("The num of Records Loaded is : " + count);
        memoryCacheImpl1.loadCacheRecords(temp);
        end = true;
    }
}