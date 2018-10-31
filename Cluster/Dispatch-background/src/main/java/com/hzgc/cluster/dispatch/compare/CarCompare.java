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
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    DispatchMapper dispatureMapper;
    @Autowired
    private DispatchRecognizeMapper dispatureRecognizeMapper;
    @Autowired
    InnerService innerService;
    @Autowired
    private Producer producer;
    @Value("${kafka.topic.dispatch-show}")
    private String topic;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            Map<String, CameraQueryDTO> map = platformService.getCameraInfoByBatchIpc(ipcIds); //1000001

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
                DispatchRecognize dispatureRecognize = new DispatchRecognize();
                dispatureRecognize.setId(UuidUtil.getUuid().substring(0, 32));
                dispatureRecognize.setDispatchId(disp.getId());
                try {
                    dispatureRecognize.setRecordTime(sdf.parse(carObject.getTimeStamp()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dispatureRecognize.setCreateTime(new Date());
                dispatureRecognize.setDeviceId(carObject.getIpcId());
                String surl = CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getsAbsolutePath());
                String burl = CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getbAbsolutePath());
                dispatureRecognize.setBurl(burl);
                dispatureRecognize.setSurl(surl);
                dispatureRecognize.setType(1);
                try {
                    dispatureRecognizeMapper.insertSelective(dispatureRecognize);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error(e.getMessage());
                }

                Dispatch dispach = dispatureMapper.selectByPrimaryKey(disp.getId());
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setDeviceId(carObject.getIpcId());
                alarmMessage.setDeviceName(map.get(carObject.getIpcId()).getCameraName());
                alarmMessage.setPlate(disp.getCar());
                alarmMessage.setType(1);
                alarmMessage.setSim(100f);
                alarmMessage.setName(dispach.getName());
                alarmMessage.setIdCard(dispach.getIdcard());
                String ip = carObject.getIp();
                alarmMessage.setBCaptureImage(CollectUrlUtil.toHttpPath(ip, "2573", carObject.getbAbsolutePath()));
                alarmMessage.setCaptureImage(CollectUrlUtil.toHttpPath(ip, "2573", carObject.getsAbsolutePath()));
                alarmMessage.setNotes(dispach.getNotes());
                alarmMessage.setId(disp.getId());
                alarmMessage.setTime(carObject.getTimeStamp());
                producer.send(topic, JacksonUtil.toJson(alarmMessage));
            }
            log.info("The size of car compared is " + carObjects.size() + " , the time is " + (System.currentTimeMillis() - start));
        }
    }
}
