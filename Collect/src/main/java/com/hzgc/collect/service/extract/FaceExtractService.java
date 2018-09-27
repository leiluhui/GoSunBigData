package com.hzgc.collect.service.extract;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureData;
import com.hzgc.jniface.PictureFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FaceExtractService {
    @Autowired
    @SuppressWarnings("unused")
    private FaceExtractService faceExtractService;

    /**
     * 特征提取
     *
     * @param imageBytes 图片的字节数组
     * @return float[] 特征值:长度为512的float[]数组
     */
    public PictureData featureExtractByImage(byte[] imageBytes) {
        PictureData pictureData = new PictureData();
        pictureData.setImageID(UuidUtil.getUuid());
        pictureData.setImageData(imageBytes);
        log.info("imageBytes: " + JSON.toJSONString(imageBytes));
        log.info("Start extract feature");
        FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(imageBytes, PictureFormat.JPG);
        log.info("ok");
        if (faceAttribute != null) {
            log.info("Face extract successful, image contains feature");
            pictureData.setFeature(faceAttribute);
            return pictureData;
        } else {
            log.info("Face extract failed, image not contains feature");
            return null;
        }
    }
}
