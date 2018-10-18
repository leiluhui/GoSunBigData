package com.hzgc.collect.service.extract;

import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.*;
import com.hzgc.seemmo.util.BASE64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
//        FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(imageBytes, PictureFormat.JPG);
        ArrayList <SmallImage> smallImages = FaceFunction.faceCheck(imageBytes, PictureFormat.JPG);
        if (null != smallImages && smallImages.size() > 0) {
            pictureData.setSmallImage(smallImages);
            pictureData.setFaceTotal(smallImages.size());
            log.info("Face extract successful, image contains feature");
            return pictureData;
        }
//        if (null != faceAttribute.getFeature() && faceAttribute.getFeature().length > 0) {
//            log.info("Face extract successful, image contains feature");
//            pictureData.setFeature(faceAttribute);
//            return pictureData;
//        }
        log.info("Face extract failed, image not contains feature");
        return null;
    }

    public PictureData featureExtractByImage(String base64Str) {
        byte[] imageBin = BASE64Util.base64Str2BinArry(base64Str);
        return featureExtractByImage(imageBin);
    }
}
