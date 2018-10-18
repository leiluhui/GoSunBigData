package com.hzgc.cluster.dispach.cache;

import com.hzgc.cluster.dispach.model.DoubleBufferQueue;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaptureCache {
//    private static CaptureCache captureCache;
    private DoubleBufferQueue<FaceObject> faceObjects; //人脸抓拍
    private DoubleBufferQueue<CarObject> carObjects; //车辆抓拍


    public CaptureCache(){
        faceObjects = new DoubleBufferQueue<>();
        carObjects = new DoubleBufferQueue<>();
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
}
