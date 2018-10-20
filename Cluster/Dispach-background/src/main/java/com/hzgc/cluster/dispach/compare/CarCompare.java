package com.hzgc.cluster.dispach.compare;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.DispachData;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.dao.DispachRecognizeMapper;
import com.hzgc.cluster.dispach.model.DispachRecognize;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private DispachRecognizeMapper dispatureRecognizeMapper;

    public CarCompare(){
        action = true;
    }

    @Override
    public void run() {
        while (action){
            List<CarObject> carObjects = captureCache.getCar();
            if(carObjects.size() == 0){
                try {
                    Thread.sleep(300);
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
                dispatureRecognize.setBurl(carObject.getbFtpUrl());
                dispatureRecognize.setSurl(carObject.getsFtpUrl());
                dispatureRecognize.setType(1);
                dispatureRecognize.setCreateTime(new Timestamp(System.currentTimeMillis()));
                dispatureRecognizeMapper.insert(dispatureRecognize);
            }
        }
    }
}
