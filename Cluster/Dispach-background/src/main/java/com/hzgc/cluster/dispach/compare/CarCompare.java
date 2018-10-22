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
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CarCompare implements Runnable{
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
    @Value("${kafka.topic.dispatch-show}")
    private String topic;

    public CarCompare(){
        action = true;
    }

    @Override
    public void run() {
        while (action){
            long start = System.currentTimeMillis();
            List<CarObject> carObjects = captureCache.getCar();
            if(carObjects.size() == 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            List<String> ipcIds = new ArrayList<>();
            for(CarObject carObject : carObjects){
                ipcIds.add(carObject.getIpcId());
            }
            Map<String, CameraQueryDTO> map = platformService.getCameraInfoByBatchIpc(ipcIds);
            for(CarObject carObject : carObjects){
                Long region = Long.parseLong(map.get(carObject.getIpcId()).getRegion());
                List<DispachData> dispatureDataList = tableCache.getCarInfo(region);
                DispachData disp = null;
                for(DispachData dispatureData : dispatureDataList){
                    if(dispatureData.getCar() != null && dispatureData.getCar().equals(carObject.getAttribute().getPlate_licence())){
                        disp = dispatureData;
                    }
                }
                if(disp == null){
                    continue;
                }
                DispachRecognize dispatureRecognize = new DispachRecognize();
                dispatureRecognize.setDispatchId(disp.getId());
                dispatureRecognize.setRecordTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognize.setDeviceId(carObject.getIpcId());
                String surl = CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getsAbsolutePath());
                String burl = CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getbAbsolutePath());
                dispatureRecognize.setBurl(burl);
                dispatureRecognize.setSurl(surl);
                dispatureRecognize.setType(1);
//                dispatureRecognize.setCreateTime(carObject.getTimeStamp());

                Dispach dispach = dispatureMapper.selectByPrimaryKey(disp.getId());
                dispatureRecognizeMapper.insert(dispatureRecognize);
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setDeviceId(carObject.getIpcId());
                alarmMessage.setDeviceName(map.get(carObject.getIpcId()).getCameraName());
                alarmMessage.setPlate(disp.getCar());
                alarmMessage.setType(1);
                alarmMessage.setSim(100f);
                alarmMessage.setName(dispach.getName());
                alarmMessage.setIdCard(dispach.getIdcard());
                String ip = innerService.hostName2Ip(carObject.getHostname()).getIp();
                alarmMessage.setCaptureImage(CollectUrlUtil.toHttpPath(ip, "2573", carObject.getbAbsolutePath()));
                alarmMessage.setId(disp.getId());
                alarmMessage.setTime(carObject.getTimeStamp());
                producer.send(topic, JacksonUtil.toJson(alarmMessage));
            }
            log.info("The size of car compared is " + carObjects.size() + " , the time is " + (System.currentTimeMillis() - start));
        }
    }
}
