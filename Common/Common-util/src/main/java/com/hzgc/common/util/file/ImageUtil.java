package com.hzgc.common.util.file;

import org.apache.log4j.Logger;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;


public class ImageUtil {

    private static Logger LOG = Logger.getLogger(ImageUtil.class);
    /**
     * @param filePath
     * @param image
     * @return  boolean  true 代表成功.false 代表失败
     */
    public static boolean save(String filePath, byte[] image){
        FileImageOutputStream imageOutput = null;
        if(filePath == null || "".equals(filePath) || image == null || image.length == 0){
            LOG.error("Image save, but path or image is null");
            return false;
        }else{
            try {
                imageOutput = new FileImageOutputStream(new File(filePath));
                imageOutput.write(image,0,image.length);
               LOG.info("Image save: "+ filePath + "    " + imageOutput.length()/1000 + "KB");
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
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
