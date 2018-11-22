package com.hzgc.cluster.dispatch.compare;

import com.hzgc.cluster.dispatch.cache.CaptureCache;
import com.hzgc.cluster.dispatch.cache.DispachData;
import com.hzgc.cluster.dispatch.cache.TableCache;
import com.hzgc.cluster.dispatch.dao.DispatchMapper;
import com.hzgc.cluster.dispatch.dao.DispatchRecognizeMapper;
import com.hzgc.cluster.dispatch.model.Dispatch;
import com.hzgc.cluster.dispatch.model.DispatchRecognize;
import com.hzgc.cluster.dispatch.producer.AlarmMessage;
import com.hzgc.cluster.dispatch.producer.Producer;
import com.hzgc.common.collect.bean.MacObject;
import com.hzgc.common.service.api.bean.DetectorQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class MacCompare implements Runnable{
    private boolean action;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private CaptureCache captureCache;
    @Autowired
    private TableCache tableCache;
    @Autowired
    private PlatformService platformService;
    @Autowired
    DispatchMapper dispatureMapper;
    @Autowired
    private DispatchRecognizeMapper dispatureRecognizeMapper;
    @Autowired
    private Producer producer;
    @Value("${kafka.topic.dispatch-show}")
    private String topic;


    public MacCompare(){
        action = true;
    }

    @Override
    public void run() {
        while (action){
            long start = System.currentTimeMillis();
            List<MacObject> macObjects = captureCache.getMac();
            if(macObjects.size() == 0){
                log.info("The size of mac for black is 0");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            List<String> sns = new ArrayList<>();
            for(MacObject macObject : macObjects){
                sns.add(macObject.getSn());
            }
            Map<String, DetectorQueryDTO> map = new HashMap<>();
            try {
                map = platformService.getInfoByBatchSn(sns);
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
                continue;
            }
            for(MacObject macObject : macObjects){
                if(macObject.getSn() == null){
                    log.error("The deviceId of captch is null");
                    continue;
                }
                if(map.get(macObject.getSn()) == null){
                    log.error("There os no region found by deviceId : " + macObject.getSn());
                    continue;
                }
                Long region = map.get(macObject.getSn()).getDistrictId();
                List<DispachData> dispatureDataList = tableCache.getMacInfo(region);
                if(dispatureDataList == null){
                    log.info("There are no captch rule for region " + region);
                    continue;
                }
                DispachData disp = null;
                for(DispachData dispatureData : dispatureDataList){
                    if(dispatureData.getMac() != null && dispatureData.getMac().equals(macObject.getMac())){
                        disp = dispatureData;
                    }
                }
                if(disp == null){
                    continue;
                }
                DispatchRecognize dispatureRecognize = new DispatchRecognize();
                dispatureRecognize.setId(UuidUtil.getUuid().substring(0, 32));
                dispatureRecognize.setDispatchId(disp.getId());
                dispatureRecognize.setRecordTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognize.setDeviceId(macObject.getSn());
                dispatureRecognize.setType(2);
                dispatureRecognize.setCreateTime(new Timestamp(macObject.getTime()));
                try {
                    dispatureRecognizeMapper.insertSelective(dispatureRecognize);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error(e.getMessage());
                }

                Dispatch dispach = dispatureMapper.selectByPrimaryKey(disp.getId());
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setDeviceId(macObject.getSn());
                alarmMessage.setDeviceName(map.get(macObject.getSn()).getDetectorName());
                alarmMessage.setMac(disp.getMac());
                alarmMessage.setType(2);
                alarmMessage.setSim(100f);
                alarmMessage.setName(dispach.getName());
                alarmMessage.setIdCard(dispach.getIdcard());
                alarmMessage.setNotes(dispach.getNotes());
                alarmMessage.setTime(sdf.format(new Date(macObject.getTime())));
                producer.send(topic, JacksonUtil.toJson(alarmMessage));
            }
            log.info("The size of mac compared is " + macObjects.size() + " , the time is " + (System.currentTimeMillis() -start));
        }
    }
}
