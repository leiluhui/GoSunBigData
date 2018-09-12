package com.hzgc.compare.worker.persistence;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HDFSStreamCache  {
    private static Logger LOG = Logger.getLogger(HDFSStreamCache.class);
    private  static HDFSStreamCache hdfsStreamCache;
    private Configuration configuration = new Configuration();
    private FileSystem fileSystem;
    private Map<Path, String> fileMap = new HashMap<>();
    private Map<Path, Long> offMap = new HashMap<>();
    private Map<Path, FSDataOutputStream> outputStreamMap = new HashMap<>();

    private HDFSStreamCache(){
        init();
    }

    public void init() {
        configuration.addResource(ClassLoader.getSystemResourceAsStream("core-site.xml"));
        try {
            fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    FSDataOutputStream getStream(Path month, Path filePath) {
        String currentFileName = filePath.getName();
        FSDataOutputStream stream = outputStreamMap.get(month);
        String file = fileMap.get(month);
        if(stream == null || file == null || !file.equals(currentFileName)) {
            LOG.info("Get OutputStream of file : " + currentFileName);
            LOG.info("The file to write before is : " + file);
            try {
                boolean exists = fileSystem.exists(filePath);
                if(exists){
//                    if(fileSystem.
                    stream = fileSystem.append(filePath);
                } else{
                    stream = fileSystem.create(filePath);
                }
                file = filePath.getName();
                fileMap.put(month, file);
                outputStreamMap.put(month, stream);
                offMap.put(month, fileSystem.getFileStatus(filePath).getLen());
                LOG.info("The offer is : " + offMap.get(month));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stream;
    }

    public void addLen(Path month, Long len){
        Long l = offMap.get(month);
        if(l != null){
            offMap.put(month, len + l);
        }
    }

    public Long getLen(Path month){
        return offMap.get(month);
    }

    public void reset(Path month){
        offMap.put(month, 0L);
    }

    FSDataInputStream getReaderStream(String file) {
        try {
            return fileSystem.open(new Path(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HDFSStreamCache getInstance(){
        if(hdfsStreamCache == null){
            hdfsStreamCache = new HDFSStreamCache();
        }
        return hdfsStreamCache;
    }
}
