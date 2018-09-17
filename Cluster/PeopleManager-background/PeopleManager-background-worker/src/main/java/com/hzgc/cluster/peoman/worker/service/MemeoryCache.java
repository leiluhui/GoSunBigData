package com.hzgc.cluster.peoman.worker.service;

import com.hzgc.cluster.peoman.worker.dao.PictureMapper;
import com.hzgc.cluster.peoman.worker.model.Picture;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MemeoryCache {
    @Autowired
    @SuppressWarnings("unused")
    private PictureMapper pictureMapper;

    @Value("face.float.threshold")
    @SuppressWarnings("unused")
    private float floatThreshold;

    @Value("face.float.compare.open")
    @SuppressWarnings("unused")
    private boolean isOpen;

    @Value("face.bit.threshold")
    @SuppressWarnings("unused")
    private float featureThreshold;

    private Map<Integer, String> indexToPictureKey = new HashMap<>();
    private LinkedList<byte[]> bitFeatureList = new LinkedList<>();
    private Map<String, ComparePicture> pictureMap = new HashMap<>();
    private AtomicInteger atomicInteger = new AtomicInteger();

    public MemeoryCache() {
        FaceFunction.init();
    }

    public void putData(ComparePicture picture) {
        int index = atomicInteger.getAndIncrement();
        bitFeatureList.add(index, picture.getBitFeature());
        indexToPictureKey.put(index, picture.getPeopleId());
        pictureMap.put(picture.getPeopleId(), picture);
    }

    public ComparePicture comparePicture(FaceAttribute faceAttribute) {
        byte[] bitFeature = faceAttribute.getBitFeature();
        if (bitFeature != null && bitFeature.length > 0) {
            byte[][] queryList = new byte[1][];
            queryList[0] = bitFeature;
            ArrayList<CompareResult> compareResList =
                    FaceFunction.faceCompareBit(bitFeatureList.toArray(new byte[0][]), queryList, 1);
            CompareResult compareResult = compareResList.get(0);
            ArrayList<FaceFeatureInfo> featureInfos = compareResult.getPictureInfoArrayList();
            FaceFeatureInfo faceFeatureInfo = featureInfos.get(0);
            int index = faceFeatureInfo.getIndex();
            String pictureKey = indexToPictureKey.get(index);
            ComparePicture comparePicture = pictureMap.get(pictureKey);
            if (isOpen) {
                Picture picture = pictureMapper.selectByPictureId(comparePicture.getId());
                String floatFeatureStr = picture.getFeature();
                if (floatFeatureStr != null && !"".equals(floatFeatureStr)) {
                    float[] floatFeature = FaceFunction.string2floatArray(floatFeatureStr);
                    if (floatFeature.length == 512 && faceAttribute.getFeature().length == 512) {
                        float sim = FaceFunction.featureCompare(floatFeature, faceAttribute.getFeature());
                        if (sim >= this.floatThreshold) {
                            return comparePicture;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                if (faceFeatureInfo.getScore() >= 90) {
                    return comparePicture;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
