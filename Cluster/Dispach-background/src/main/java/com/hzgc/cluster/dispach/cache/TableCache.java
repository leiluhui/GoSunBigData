package com.hzgc.cluster.dispach.cache;

import com.hzgc.cluster.dispach.dao.DispachMapper;
import com.hzgc.cluster.dispach.model.Dispach;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TableCache {
    @Autowired
    private DispachMapper dispachMapper;
    //    private static TableCache tableCache;
    private Map<Long, List<DispachData>> faceInfos; //人脸布控信息
    private Map<Long, byte[][]> faceFeatures;
    private Map<Long, List<DispachData>> carInfos;  //车辆布控信息
    private Map<Long, List<DispachData>> macInfos;  //Mac布控信息

    public void loadData(){
        List<Dispach> searchResult = dispachMapper.selectAll();
        faceInfos = new HashMap<>();
        faceFeatures = new HashMap<>();
        carInfos = new HashMap<>();
        macInfos = new HashMap<>();
        for(Dispach dispature : searchResult){
            Long region = dispature.getRegion();
            //启动的人脸布控
            if(dispature.getStatus() == 0 && dispature.getBit_feature() != null){
                List<DispachData> deployList = faceInfos.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setBitfeature(dispature.getBit_feature());
                deployList.add(data);
            }
            //车辆布控
            if (dispature.getStatus() == 0  && dispature.getCar() != null){
                List<DispachData> deployList = carInfos.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setCar(dispature.getCar());
                deployList.add(data);
            }
            //Mac布控
            if (dispature.getStatus() == 0 && dispature.getMac() != null){
                List<DispachData> deployList = macInfos.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setMac(dispature.getMac());
                deployList.add(data);
            }
        }

        for(Map.Entry<Long, List<DispachData>> entry : faceInfos.entrySet()){
            List<DispachData> list = entry.getValue();
            byte[][] features = new byte[list.size()][32];
            int index = 0;
            for(DispachData dispatureData : list){
                byte[] feature = FaceUtil.base64Str2BitFeature(dispatureData.getBitfeature());
                features[index] = feature;
                dispatureData.setIndex(index);
            }
            faceFeatures.put(entry.getKey(), features);
        }
    }

    public void showFaceInfo(){
        for(Map.Entry<Long, List<DispachData>> entry : faceInfos.entrySet()){
            Long region = entry.getKey();
            List<DispachData> list = entry.getValue();
            for(DispachData dispachData : list){
                System.out.println("Region : " + region);
                System.out.println("FaceInfo : " + dispachData.toString());
            }
        }
    }

    public void showFeatures(){
        for(Map.Entry<Long, byte[][]> entry : faceFeatures.entrySet()){
            Long region = entry.getKey();
            System.out.println("Region : " + region);
            System.out.println("Feature : " + Arrays.toString(entry.getValue()));
        }
    }

    public void showCarInfo(){
        for(Map.Entry<Long, List<DispachData>> entry : carInfos.entrySet()){
            Long region = entry.getKey();
            List<DispachData> list = entry.getValue();
            for(DispachData dispachData : list){
                System.out.println("Region : " + region);
                System.out.println("FaceInfo : " + dispachData.toString());
            }
        }
    }

    public void showMacInfo(){
        for(Map.Entry<Long, List<DispachData>> entry : macInfos.entrySet()){
            Long region = entry.getKey();
            List<DispachData> list = entry.getValue();
            for(DispachData dispachData : list){
                System.out.println("Region : " + region);
                System.out.println("FaceInfo : " + dispachData.toString());
            }
        }
    }


    /**
     * 增加车辆布控
     * @param id 布控Id
     * @param region 布控地区
     * @param car 布控车辆
     */
    public void addCar(String id, Long region, String car){
        List<DispachData> list = carInfos.get(region);
        DispachData dispatureData = new DispachData();
        dispatureData.setId(id);
        dispatureData.setCar(car);
        list.add(dispatureData);
    }

    /**
     * 增加Mac布控
     * @param id 布控Id
     * @param region 布控地区
     * @param mac 布控Mac
     */
    public void addMac(String id, Long region, String mac){
        List<DispachData> list = macInfos.get(region);
        DispachData dispatureData = new DispachData();
        dispatureData.setId(id);
        dispatureData.setMac(mac);
        list.add(dispatureData);
    }

    /**
     * 增加人脸布控
     * @param id 布控Id
     * @param region 布控地区
     * @param bitFeature 布控人脸特征
     */
    public void addFace(String id, Long region, String bitFeature){
        List<DispachData> carinfo = carInfos.get(region);
        DispachData dispatureData = new DispachData();
        dispatureData.setId(id);
        dispatureData.setBitfeature(bitFeature);
        dispatureData.setIndex(carInfos.size());
        carinfo.add(dispatureData);

        byte[][] features = faceFeatures.get(region);
        byte[][] newFeaures = new byte[features.length + 1][32];
        System.arraycopy(features, 0, newFeaures, 0, features.length);
        newFeaures[features.length] = FaceUtil.base64Str2BitFeature(bitFeature);
        faceFeatures.put(region, newFeaures);
    }

    /**
     * 删除Mac布控和车辆布控
     * @param id 布控Id
     */
    public void deleteDispature(String id){
        DispachData carToReove = null;
        Long region = 0L;
        for(Map.Entry<Long, List<DispachData>> entry : carInfos.entrySet()){
            List<DispachData> carInfo  = entry.getValue();
            for(DispachData car : carInfo){
                if(car.getId()!= null && car.getId().equals(id)){
                    carToReove = car;
                    region = entry.getKey();
                }
            }
        }
        if(carToReove != null){
            carInfos.get(region).remove(carToReove);
        }

        DispachData macToReove = null;
        for(Map.Entry<Long, List<DispachData>> entry : macInfos.entrySet()){
            List<DispachData> macInfo  = entry.getValue();
            for(DispachData mac : macInfo){
                if(mac.getId()!= null && mac.getId().equals(id)){
                    macToReove = mac;
                    region = entry.getKey();
                }
            }
        }
        if(carToReove != null){
            macInfos.get(region).remove(macToReove);
        }
    }

    public void deleteFaceDispature(String id){
        DispachData faceToReove = null;
        Long region = 0L;
        for(Map.Entry<Long, List<DispachData>> entry : faceInfos.entrySet()){
            List<DispachData> faceInfo  = entry.getValue();
            for(DispachData face : faceInfo){
                if(face.getId()!= null && face.getId().equals(id)){
                    faceToReove = face;
                    region = entry.getKey();
                }
            }
        }
        List<DispachData> faceInfo  = faceInfos.get(region);
        if(faceToReove != null) {
            faceInfo.remove(faceToReove);
            int removeIndex = faceToReove.getIndex();
            byte[][] features = faceFeatures.get(region);
            byte[][] newFeatures = new byte[features.length - 1][32];
            System.arraycopy(features, 0, newFeatures, 0, removeIndex);
            System.arraycopy(features, removeIndex + 1, newFeatures, removeIndex, features.length - 1 - removeIndex);
            faceFeatures.put(region, newFeatures);
        }
    }

    public byte[][] getFeatures(Long region){
        return faceFeatures.get(region);
    }

    /**
     * 根据序列号返回Id
     * @param index 序列号
     * @return Id
     */
    public String getIdByIndex(Long region, Integer index){
        List<DispachData> list = faceInfos.get(region);
        return list.get(index).getId();
    }

    public List<DispachData> getCarInfo(Long region){
        return carInfos.get(region);
    }

    public List<DispachData> getMacInfo(Long region){
        return macInfos.get(region);
    }

//    public static TableCache getInstance(){
//        if(tableCache == null){
//            tableCache = new TableCache();
//        }
//        return tableCache;
//    }
}
