package com.hzgc.cluster.dispach.compare;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.DispachData;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.dao.DispachMapper;
import com.hzgc.cluster.dispach.dao.DispachRecognizeMapper;
import com.hzgc.cluster.dispach.model.Dispach;
import com.hzgc.cluster.dispach.model.DispachRecognize;
import com.hzgc.cluster.dispach.producer.AlarmMessage;
import com.hzgc.cluster.dispach.producer.Producer;
import com.hzgc.common.collect.bean.MacObject;
import com.hzgc.common.service.api.bean.DetectorQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    DispachMapper dispatureMapper;
    @Autowired
    private DispachRecognizeMapper dispatureRecognizeMapper;
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
            Map<String, DetectorQueryDTO> map = platformService.getInfoByBatchSn(sns);
            for(MacObject macObject : macObjects){
                Long region = Long.parseLong(map.get(macObject.getSn()).getRegion());
                List<DispachData> dispatureDataList = tableCache.getMacInfo(region);
                DispachData disp = null;
                for(DispachData dispatureData : dispatureDataList){
                    if(dispatureData.getMac() != null && dispatureData.getMac().equals(macObject.getMac())){
                        disp = dispatureData;
                    }
                }
                if(disp == null){
                    continue;
                }
                DispachRecognize dispatureRecognize = new DispachRecognize();
                dispatureRecognize.setDispatchId(disp.getId());
                dispatureRecognize.setRecordTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognize.setDeviceId(macObject.getSn());
                dispatureRecognize.setType(2);
                dispatureRecognize.setCreateTime(new Timestamp(macObject.getTimestamp()));
                dispatureRecognizeMapper.insertSelective(dispatureRecognize);
                Dispach dispach = dispatureMapper.selectByPrimaryKey(disp.getId());
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setDeviceId(macObject.getSn());
                alarmMessage.setDeviceName(map.get(macObject.getSn()).getDetectorName());
                alarmMessage.setMac(disp.getMac());
                alarmMessage.setType(2);
                alarmMessage.setSim(100f);
                alarmMessage.setName(dispach.getName());
                alarmMessage.setIdCard(dispach.getIdcard());
                alarmMessage.setTime(sdf.format(new Date(macObject.getTimestamp())));
                producer.send(topic, JacksonUtil.toJson(alarmMessage));
            }
            log.info("The size of mac compared is " + macObjects.size() + " , the time is " + (System.currentTimeMillis() -start));
        }
    }
}
