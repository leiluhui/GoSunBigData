package com.hzgc.collect.service.http;

import com.hzgc.common.util.basic.FileUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class HttpFile {

    public byte[] getImage(String filePath) {
        return FileUtil.fileToByteArray(filePath);
    }


    public byte[] getZipImage(String filePath) {
        return getZipImage(filePath, true, 100, 100);
    }

    /**
     *
     * @param filePath 图片路径
     * @param proportion 是否等比缩放标记
     * @param outPutWidth 默认输出图片宽
     * @param outPutHeight 默认输出图片高
     * @return 图片二进制数据
     */
    public byte[] getZipImage(String filePath, boolean proportion, int outPutWidth, int outPutHeight) {
        File file = new File(filePath);
        try {
            //获取图片文件
            Image img = ImageIO.read(file);
            //判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                log.error("Can't read this image, file path is:" + filePath);
                return new byte[0];
            } else {
                int newWidth;
                int newHeight;
                //判断是否等比缩放
                if (proportion) {
                    //为等比缩放计算输出的图片宽度及高度
                    double rate1 = ((double) img.getWidth(null)) / (double) outPutWidth + 0.1;
                    double rate2 = ((double) img.getHeight(null)) / (double) outPutHeight + 0.1;
                    //根据缩放比率大的进行缩放控制
                    double rate = rate1 > rate2 ? rate1 : rate2;
                    newWidth = (int) (((double) img.getWidth(null)) / rate);
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                } else {
                    newWidth = outPutWidth; //输出的图片宽度
                    newHeight = outPutHeight; //输出的图片的高度
                }
                BufferedImage tag = new BufferedImage(newWidth,
                        newHeight, BufferedImage.TYPE_INT_RGB);
                //Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
                tag.getGraphics().drawImage(
                        img.getScaledInstance(newWidth, newHeight,
                                Image.SCALE_SMOOTH), 0, 0, null);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
                return out.toByteArray();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new byte[0];
    }
}
