package com.hzgc.compare.worker.persistence;


import java.io.*;

public class LocalStreamCache {
    private  static LocalStreamCache localStreamCache;
    private BufferedWriter bufferedWriter;
    private File file;

    private LocalStreamCache() {
    }

    BufferedWriter getWriterStream(File file) {
        if(bufferedWriter == null || !file.equals(this.file)) {
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                this.file = file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bufferedWriter;
    }

    BufferedReader getReaderStream(File file) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return bufferedReader;
    }

    public static LocalStreamCache getInstance(){
        if(localStreamCache == null){
            localStreamCache = new LocalStreamCache();
        }
        return localStreamCache;
    }
}
