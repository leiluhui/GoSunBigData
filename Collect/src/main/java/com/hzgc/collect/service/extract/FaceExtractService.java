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
    public BigPictureData featureExtractByImage(byte[] imageBytes) {
        String imageType = null;
        BigPictureData bigPictureData = new BigPictureData();
        ArrayList <PictureData> smallPictures = new ArrayList <>();
        ArrayList <SmallImage> smallImages = FaceFunction.faceCheck(imageBytes, PictureFormat.JPG);
        if (null != smallImages && smallImages.size() > 0) {
            for (SmallImage smallImage: smallImages) {
                PictureData pictureData = new PictureData();
                pictureData.setImageData(smallImage.getPictureStream());
                pictureData.setImageID(UuidUtil.getUuid());
                pictureData.setFeature(smallImage.getFaceAttribute());
                imageType = smallImage.getImageType();
                smallPictures.add(pictureData);
            }
            bigPictureData.setImageType(imageType);
            bigPictureData.setSmallImage(smallPictures);
            bigPictureData.setFaceTotal(smallPictures.size());
            bigPictureData.setImageID(UuidUtil.getUuid());
            bigPictureData.setImageData(imageBytes);
            log.info("Face extract successful, image contains feature");
            return bigPictureData;
        }
        log.info("Face extract failed, image not contains feature");
        return null;
    }

    public PictureData featureExtractByImage(String base64Str) {
        byte[] imageBin = BASE64Util.base64Str2BinArry(base64Str);
        return featureExtractByImage(imageBin);
    }
}
