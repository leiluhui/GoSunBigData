package com.hzgc.cluster.dispatch.consumer;

import com.hzgc.cluster.dispatch.cache.CaptureCache;
import com.hzgc.cluster.dispatch.cache.TableCache;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.bean.MacObject;
import com.hzgc.common.collect.bean.PersonObject;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;


@Component
@Slf4j
public class KafkaComsumer {
    private ReentrantLock writeLock = new ReentrantLock();

    @Autowired
    CaptureCache captureCache;
    @Autowired
    TableCache tableCache;

    @KafkaListener(topics = {"${kafka.topic.face}"}, groupId = "${kafka.data.group.id}")
    public void receiveFace(String message){
        FaceObject faceObject = JacksonUtil.toObject(message, FaceObject.class);
        captureCache.pushFace(faceObject);
        captureCache.pushFaceObjectsForLive(faceObject);
        captureCache.pushFaceObjectsForWhite(faceObject);
    }

    @KafkaListener(topics = {"${kafka.topic.car}"}, groupId = "${kafka.data.group.id}")
    public void receiveCar(String message){
        CarObject carObject = JacksonUtil.toObject(message, CarObject.class);
        captureCache.pushCar(carObject);
        captureCache.pushCarObjectsForLive(carObject);
    }

    @KafkaListener(topics = {"${kafka.topic.mac}"}, groupId = "${kafka.data.group.id}")
    public void receiveMac(String message){
        MacObject macObject = JacksonUtil.toObject(message, MacObject.class);
        captureCache.pushMac(macObject);
    }

    @KafkaListener(topics = {"${kafka.topic.person}"}, groupId = "${kafka.data.group.id}")
    public void receivePerson(String message){
        PersonObject personObject = JacksonUtil.toObject(message, PersonObject.class);
        captureCache.pushPersonObjectForLive(personObject);
    }

    @KafkaListener(topics = {"${kafka.topic.command}"})
    public void receiveCommand(ConsumerRecord<String, String> record){
        String key = record.key();
        KafkaMessage messageObj = JacksonUtil.toObject(record.value(), KafkaMessage.class);
        switch (key){
            case "ADD" :
//                log.info("Add a dispach");
                addDispach(messageObj);
//                tableCache.showCarInfo(messageObj.getRegionId());
//                tableCache.showFaceInfo(messageObj.getRegionId());
//                tableCache.showFeatures(messageObj.getRegionId());
//                tableCache.showMacInfo(messageObj.getRegionId());
                break;
            case "DELETE" :
                deleteDispach(messageObj);
//                tableCache.showCarInfo(messageObj.getRegionId());
//                tableCache.showFaceInfo(messageObj.getRegionId());
//                tableCache.showFeatures(messageObj.getRegionId());
//                tableCache.showMacInfo(messageObj.getRegionId());
                break;
            case "UPDATE" :
                updateDispach(messageObj);
//                tableCache.showCarInfo(messageObj.getRegionId());
//                tableCache.showFaceInfo(messageObj.getRegionId());
//                tableCache.showFeatures(messageObj.getRegionId());
//                tableCache.showMacInfo(messageObj.getRegionId());
                break;
            case "RE_LOAD" :
                tableCache.loadData();
                break;
            case "START" :
                startDispach(messageObj);
                break;
            case "STOP" :
                stopDispach(messageObj);
                break;
            case "SHOW" :
                tableCache.showCarInfo(messageObj.getRegionId());
                tableCache.showFaceInfo(messageObj.getRegionId());
                tableCache.showFeatures(messageObj.getRegionId());
                tableCache.showMacInfo(messageObj.getRegionId());
                break;
            case "DISPATCH_LIVE_UPDATE" :  //活体布控重新加载
                tableCache.loadDispatchLive();
                break;
            case "DISPATCH_WHITE_UPDATE" : //白名单布控重新加载
                tableCache.loadDispatchWhite();
                break;
            default :
                break;
        }
    }

    private void addDispach(KafkaMessage messageObj){
        writeLock.lock();
        try {
            String id = messageObj.getId();
            Long region = messageObj.getRegionId();
            if(messageObj.getMac() != null){
                tableCache.addMac(id, region, messageObj.getMac());
            }
            if(messageObj.getCar() != null){ ;
                tableCache.addCar(id, region, messageObj.getCar());
            }
            if(messageObj.getBitFeature() != null){
                tableCache.addFace(id, region, messageObj.getBitFeature());
            }
        }finally {
            writeLock.unlock();
        }

    }

    private void deleteDispach(KafkaMessage messageObj){
        writeLock.lock();
        try {
            tableCache.deleteFaceDispature(messageObj.getId());
            tableCache.deleteDispature(messageObj.getId());
        }catch (Exception e){
            tableCache.loadData();
        }
        finally {
            writeLock.unlock();
        }
    }

    private void updateDispach(KafkaMessage messageObj){
        log.info("Update message id : " + messageObj.getId());
        writeLock.lock();
        try {
            deleteDispach(messageObj);
            addDispach(messageObj);
        }finally {
            writeLock.unlock();
        }

    }

    private void startDispach(KafkaMessage messageObj){
        addDispach(messageObj);
    }

    private void stopDispach(KafkaMessage messageObj){
        deleteDispach(messageObj);
    }

    public static void main(String args[]){
        String json = "{\"id\":\"636F93ED33A94647A87A3A624C0E288E\",\"regionId\":null,\"bitFeature\":null,\"car\":null,\"mac\":null}";
        KafkaMessage messageObj = JacksonUtil.toObject(json, KafkaMessage.class);
        System.out.println(messageObj);
    }
}
