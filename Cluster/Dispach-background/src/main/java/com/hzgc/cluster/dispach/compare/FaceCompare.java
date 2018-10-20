package com.hzgc.cluster.dispach.compare;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.dao.DispachMapper;
import com.hzgc.cluster.dispach.dao.DispachRecognizeMapper;
import com.hzgc.cluster.dispach.model.Dispach;
import com.hzgc.cluster.dispach.model.DispachRecognize;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FaceCompare implements Runnable{
    private boolean action;
    @Autowired
    private CaptureCache captureCache;
    @Autowired
    private TableCache tableCache;
    @Autowired
    private PlatformService platformService;
    @Autowired
    DispachMapper dispatureMapper;
    @Autowired
    private DispachRecognizeMapper dispatureRecognizeMapper;
    @Value("${simple.max}")
    private float simple_max;
    @Value("${first.compare.size}")
    private int sizeFirstCompareResult;

    public FaceCompare(){
        action = true;
    }

    @Override
    public void run() {
        while (action){
            List<FaceObject> faceObjects = captureCache.getFace();
            if(faceObjects.size() == 0){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            List<String> ipcIds = new ArrayList<>();
            for(FaceObject faceObject : faceObjects){
                ipcIds.add(faceObject.getIpcId());
            }

            Map<String, CameraQueryDTO> map = platformService.getCameraInfoByBatchIpc(ipcIds);
            for(FaceObject faceObject : faceObjects){
                Long region = Long.parseLong(map.get(faceObject.getIpcId()).getRegion());
                byte[][] queryList = new byte[1][32];
                queryList[0] = faceObject.getAttribute().getBitFeature();
                byte[][] features = tableCache.getFeatures(region);
                if(features == null){
                    continue;
                }
                ArrayList<CompareResult> list = FaceFunction.faceCompareBit(features, queryList, sizeFirstCompareResult);
                List<String> ids = new ArrayList<>();
                for(FaceFeatureInfo faceFeatureInfo : list.get(0).getPictureInfoArrayList()){
                    String id = tableCache.getIdByIndex(region, faceFeatureInfo.getIndex());
                    ids.add(id);
                }
                List<Dispach> dispatures = dispatureMapper.selectByIds(ids);
                float sim = 0.0f;
                Dispach disp = new Dispach();
                for(Dispach dispature : dispatures){
                    float[] fea = FaceUtil.base64Str2floatFeature(dispature.getFeature());
                    float simTemp = FaceUtil.featureCompare(faceObject.getAttribute().getFeature(), fea);
                    if(simTemp > sim && simTemp > dispature.getThreshold()){
                        sim = simTemp;
                        disp = dispature;
                    }
                }
                if(sim <= simple_max){
                    continue;
                }
                DispachRecognize dispatureRecognize = new DispachRecognize();
                dispatureRecognize.setDispatchId(disp.getId());
                dispatureRecognize.setRecordTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognize.setDeviceId(faceObject.getIpcId());
                dispatureRecognize.setBurl(faceObject.getbFtpUrl());
                dispatureRecognize.setSurl(faceObject.getsFtpUrl());
                dispatureRecognize.setSimilarity(sim);
                dispatureRecognize.setType(0);
                dispatureRecognize.setCreateTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognizeMapper.insert(dispatureRecognize);
            }
        }
    }
}
