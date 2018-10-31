package com.hzgc.common.util.basic;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageUtil {

    /**
     * @param filePath 文件路径
     * @param image 图片二进制数据
     * @return boolean  true 代表成功.false 代表失败
     */
    public static boolean save(String filePath, byte[] image) {
        FileImageOutputStream imageOutput = null;
        if (filePath == null || "".equals(filePath) || image == null || image.length == 0) {
            log.error("Image save failed, but path or image is null");
            return false;
        } else {
            File file = new File(filePath);
            if (file.exists()) {
                log.error("Image save failed, file exists, file path is:{}", filePath);
                return false;
            }
            try {
                imageOutput = new FileImageOutputStream(file);
                imageOutput.write(image, 0, image.length);
                log.info("Image save: " + filePath + "    " + imageOutput.length() / 1000 + "KB");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (imageOutput != null) {
                        imageOutput.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
