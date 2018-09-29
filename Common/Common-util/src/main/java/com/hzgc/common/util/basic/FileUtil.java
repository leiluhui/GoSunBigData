package com.hzgc.common.util.basic;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileUtil {

    public static byte[] fileToByteArray(String filePath) {
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try {
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                imageFile.mkdir();
            }
            fis = new FileInputStream(imageFile);
            baos = new ByteArrayOutputStream();
            int len;
            while ((len = fis.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return bytes;
    }
}
