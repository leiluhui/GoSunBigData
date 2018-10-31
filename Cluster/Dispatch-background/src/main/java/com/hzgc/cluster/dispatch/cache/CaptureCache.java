package com.hzgc.cluster.dispatch.cache;

import com.hzgc.cluster.dispatch.model.BatchBufferQueue;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.bean.MacObject;
import com.hzgc.common.collect.bean.PersonObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaptureCache {
    //    private static CaptureCache captureCache;
    private BatchBufferQueue<FaceObject> faceObjects; //人脸抓拍 用于黑名单
    private BatchBufferQueue<CarObject> carObjects; //车辆抓拍 用于黑名单
    private BatchBufferQueue<MacObject> macObjects; //mac抓拍 用于黑名单
    private BatchBufferQueue<FaceObject> faceObjectsForWhite; //人脸用于布控白名单
    private BatchBufferQueue<FaceObject> faceObjectsForLive; //人脸用于活体检测
    private BatchBufferQueue<CarObject> carObjectsForLive; //车辆用于活体检测
    private BatchBufferQueue<PersonObject> personObjectForLive; //行人用于活体检测


    public CaptureCache(){
        faceObjects = new BatchBufferQueue<>();
        carObjects = new BatchBufferQueue<>();
        macObjects = new BatchBufferQueue<>();
        faceObjectsForWhite = new BatchBufferQueue<>();
        faceObjectsForLive = new BatchBufferQueue<>();
        carObjectsForLive = new BatchBufferQueue<>();
        personObjectForLive = new BatchBufferQueue<>();
    }

//    public static CaptureCache getInstance(){
//        if(captureCache == null){
//            captureCache = new CaptureCache();
//        }
//        return captureCache;
//    }

    public void pushFeces(List<FaceObject> list){
        faceObjects.push(list);
    }

    public void pushFace(FaceObject faceObject){
        faceObjects.push(faceObject);
    }

    public List<FaceObject> getFace(){
        return faceObjects.get();
    }

    public void pushCars(List<CarObject> list){
        carObjects.push(list);
    }

    public void pushCar(CarObject carObject){
        carObjects.push(carObject);
    }

    public List<CarObject> getCar(){
        return carObjects.get();
    }

    public void pushMacs(List<MacObject> list){
        macObjects.push(list);
    }

    public void pushMac(MacObject macObject){
        macObjects.push(macObject);
    }

    public List<MacObject> getMac(){
        return macObjects.get();
    }

    public void pushFaceObjectsForWhite(FaceObject faceObject){
        faceObjectsForWhite.push(faceObject);
    }

    public List<FaceObject> getFaceObjectsForWhite(){
        return faceObjectsForWhite.get();
    }

    public void pushFaceObjectsForLive(FaceObject faceObject){
        faceObjectsForLive.push(faceObject);
    }

    public List<FaceObject> getFaceObjectsForLive(){
        return faceObjectsForLive.get();
    }

    public void pushCarObjectsForLive(CarObject carObject){
        carObjectsForLive.push(carObject);
    }

    public List<CarObject> getCarObjectsForLive(){
        return carObjectsForLive.get();
    }

    public void pushPersonObjectForLive(PersonObject personObject){
        personObjectForLive.push(personObject);
    }

    public List<PersonObject> getPersonObjectForLive(){
        return personObjectForLive.get();
    }
}
