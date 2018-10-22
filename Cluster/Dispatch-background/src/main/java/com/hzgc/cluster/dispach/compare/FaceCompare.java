package com.hzgc.cluster.dispach.compare;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.dao.DispachMapper;
import com.hzgc.cluster.dispach.dao.DispachRecognizeMapper;
import com.hzgc.cluster.dispach.model.Dispach;
import com.hzgc.cluster.dispach.model.DispachRecognize;
import com.hzgc.cluster.dispach.producer.AlarmMessage;
import com.hzgc.cluster.dispach.producer.Producer;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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
    @Autowired
    InnerService innerService;
    @Autowired
    private Producer producer;
    @Value("${first.compare.size}")
    private int sizeFirstCompareResult;
    @Value("${kafka.topic.dispatch-show}")
    private String topic;

    public FaceCompare(){
        action = true;
    }

    @Override
    public void run() {
        while (action){
            long start = System.currentTimeMillis();
            List<FaceObject> faceObjects = captureCache.getFace();
            if(faceObjects.size() == 0){
                try {
                    Thread.sleep(500);
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
                if(sim == 0.0f){
                    continue;
                }

                DispachRecognize dispatureRecognize = new DispachRecognize();
                dispatureRecognize.setDispatchId(disp.getId());
                dispatureRecognize.setRecordTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognize.setDeviceId(faceObject.getIpcId());
                String surl = CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath());
                String burl = CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath());
                dispatureRecognize.setBurl(burl);
                dispatureRecognize.setSurl(surl);
                dispatureRecognize.setSimilarity(sim);
                dispatureRecognize.setType(0);
//                dispatureRecognize.setCreateTime(faceObject.getTimeStamp());
                dispatureRecognizeMapper.insertSelective(dispatureRecognize);
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setDeviceId(faceObject.getIpcId());
                alarmMessage.setDeviceName(map.get(faceObject.getIpcId()).getCameraName());
                alarmMessage.setType(0);
                alarmMessage.setSim(sim);
                alarmMessage.setName(disp.getName());
                alarmMessage.setIdCard(disp.getIdcard());
                String ip = innerService.hostName2Ip(faceObject.getHostname()).getIp();
                alarmMessage.setCaptureImage(CollectUrlUtil.toHttpPath(ip, "2573", faceObject.getbAbsolutePath()));
                alarmMessage.setId(disp.getId());
                alarmMessage.setTime(faceObject.getTimeStamp());
                producer.send(topic, JacksonUtil.toJson(alarmMessage));
            }
            log.info("The size of face compared is " + faceObjects.size() + " , the time is " + (System.currentTimeMillis() - start));
        }
    }
}
