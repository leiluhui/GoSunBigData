package com.hzgc.common.util.file;

import java.io.*;

public class FileUtil {

    public static byte[] fileToByteArray(String filePath){
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try {
            File imageFile = new File(filePath);
            fis = new FileInputStream(imageFile);
            baos = new ByteArrayOutputStream();
            int len;
            while((len = fis.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
}
