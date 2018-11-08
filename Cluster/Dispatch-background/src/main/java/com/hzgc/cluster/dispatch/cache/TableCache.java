package com.hzgc.cluster.dispatch.cache;

import com.hzgc.cluster.dispatch.dao.*;
import com.hzgc.cluster.dispatch.model.*;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TableCache {
    @Autowired
    private DispatchMapper dispachMapper;
    @Autowired
    private DispatchAliveMapper dispachAliveMapper;
    @Autowired
    private DispatchWhiteMapper dispatchWhiteMapper;
    @Autowired
    private DispatchWhiteinfoMapper dispatchWhiteinfoMapper;
    //    private static TableCache tableCache;

    private Map<Long, List<DispachData>> faceInfos; //人脸布控信息
    private Map<Long, byte[][]> faceFeatures;
    private Map<Long, List<DispachData>> carInfos;  //车辆布控信息
    private Map<Long, List<DispachData>> macInfos;  //Mac布控信息
    private Map<String, DispatchAlive> dispachAlives; //活体检测布控
    private Map<String, Set<float[]>> ipcToFeatures; //白名单布控

    /**
     * 加载白名单布控数据
     */
    public void loadDispatchWhite(){
        log.info("Load table for white list to cache.");
        Map<String, Set<float[]>> temp = new HashMap<>();
        List<DispatchWhite> dispatchWhites = dispatchWhiteMapper.selectAll();
        if(dispatchWhites == null){
            ipcToFeatures = temp;
            return;
        }
        log.info("Load table to cache. The size is " + dispatchWhites.size());
        for(DispatchWhite dispatchWhite : dispatchWhites){
            if(dispatchWhite.getStatus() != 0){
                continue;
            }
            String dispatchWhiteId = dispatchWhite.getId();
            String devices = dispatchWhite.getDevices();
            if(devices == null || "".equals(devices)){
                continue;
            }
            List<String> deviceList = Arrays.asList(devices.split(","));
            if(deviceList.size() == 0){
                continue;
            }
            List<DispatchWhiteinfo> dispatchWhiteinfos = dispatchWhiteinfoMapper.selectByWhiteId(dispatchWhiteId);
            if(dispatchWhiteinfos == null || dispatchWhiteinfos.size() == 0){
                continue;
            }
            for(String device : deviceList){
                Set<float[]> set = temp.computeIfAbsent(device, k -> new HashSet<>());
                for(DispatchWhiteinfo dispatchName : dispatchWhiteinfos){
                    set.add(FaceUtil.base64Str2floatFeature(dispatchName.getFeature()));
                }
            }
        }

        ipcToFeatures = temp;
    }

    /**
     * 加载黑名单布控数据
     */
    public void loadData(){
        log.info("Load table for black list to cache.");
        List<Dispatch> searchResult = dispachMapper.selectAll();
        Map<Long, List<DispachData>> faceInfosTemp = new HashMap<>();
        Map<Long, byte[][]>  faceFeaturesTemp = new HashMap<>();
        Map<Long, List<DispachData>> carInfosTemp = new HashMap<>();
        Map<Long, List<DispachData>> macInfosTemp = new HashMap<>();
        if(searchResult == null){
            return;
        }
        log.info("Load table to cache. The size is " + searchResult.size());
        for(Dispatch dispature : searchResult){
            if(dispature.getStatus() != 0){
                continue;
            }
            Long region = dispature.getRegion();
            //启动的人脸布控
            if(dispature.getStatus() == 0 && dispature.getBitFeature() != null){
                List<DispachData> deployList = faceInfosTemp.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setBitfeature(dispature.getBitFeature());
                deployList.add(data);
            }
            //车辆布控
            if (dispature.getStatus() == 0  && dispature.getCar() != null){
                List<DispachData> deployList = carInfosTemp.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setCar(dispature.getCar());
                deployList.add(data);
            }
            //Mac布控
            if (dispature.getStatus() == 0 && dispature.getMac() != null){
                List<DispachData> deployList = macInfosTemp.computeIfAbsent(region, k -> new ArrayList<>());
                DispachData data = new DispachData();
                data.setId(dispature.getId());
                data.setMac(dispature.getMac());
                deployList.add(data);
            }
        }

        for(Map.Entry<Long, List<DispachData>> entry : faceInfosTemp.entrySet()){
            List<DispachData> list = entry.getValue();
            byte[][] features = new byte[list.size()][32];
            int index = 0;
            for(DispachData dispatureData : list){
                byte[] feature = FaceUtil.base64Str2BitFeature(dispatureData.getBitfeature());
                features[index] = feature;
                dispatureData.setIndex(index);
            }
            faceFeaturesTemp.put(entry.getKey(), features);
        }

        faceInfos = faceInfosTemp;
        faceFeatures = faceFeaturesTemp;
        carInfos = carInfosTemp;
        macInfos = macInfosTemp;
    }

    /**
     * 加载活体检测布控规则
     */
    public void loadDispatchLive(){
        log.info("Load table for Live check to cache.");
        Map<String, DispatchAlive> temp = new HashMap<>();
        List<DispatchAlive> list = dispachAliveMapper.selectAll();
        if(list == null){
            dispachAlives = temp;
            return;
        }
        log.info("Load table to cache. The size is " + list.size());
        for(DispatchAlive dispachAlive : list){
            if(dispachAlive.getStatus() == 0){
                String devices = dispachAlive.getDevices();
                if(devices == null || devices.equals("")){
                    continue;
                }
                String[] deviceArr = devices.split(",");
                if(deviceArr.length == 0){
                    continue;
                }
                for(String devece : deviceArr){
                    temp.put(devece, dispachAlive);
                }
            }
        }
        dispachAlives = temp;
    }

    public void showFaceInfo(Long region){
        log.info("Show face cache info.");
        List<DispachData> list = faceInfos.get(region);
        for(DispachData dispachData : list){
            log.info("Region : " + region);
            log.info("FaceInfo : " + dispachData.toString());
        }
//        for(Map.Entry<Long, List<DispachData>> entry : faceInfos.entrySet()){
//            Long region = entry.getKey();
//            List<DispachData> list = entry.getValue();
//            for(DispachData dispachData : list){
//                log.info("Region : " + region);
//                log.info("FaceInfo : " + dispachData.toString());
//            }
//        }
    }

    public void showFeatures(Long region){
        log.info("Show feature cache info.");
        log.info("Feature : " + Arrays.toString(faceFeatures.get(region)));

//        for(Map.Entry<Long, byte[][]> entry : faceFeatures.entrySet()){
//            Long region = entry.getKey();
//            log.info("Region : " + region);
//            log.info("Feature : " + Arrays.toString(entry.getValue()));
//        }
    }

    public void showCarInfo(Long region){
        log.info("Show car cache info.");
        List<DispachData> list = carInfos.get(region);
        for(DispachData dispachData : list){
            log.info("FaceInfo : " + dispachData.toString());
        }

//        for(Map.Entry<Long, List<DispachData>> entry : carInfos.entrySet()){
//            Long region = entry.getKey();
//            List<DispachData> list = entry.getValue();
//            for(DispachData dispachData : list){
//                log.info("Region : " + region);
//                log.info("FaceInfo : " + dispachData.toString());
//            }
//        }
    }

    public void showMacInfo(Long region){
        log.info("Show mac cache info.");

        List<DispachData> list = macInfos.get(region);
        for(DispachData dispachData : list){
            log.info("FaceInfo : " + dispachData.toString());
        }
//        for(Map.Entry<Long, List<DispachData>> entry : macInfos.entrySet()){
//            Long region = entry.getKey();
//            List<DispachData> list = entry.getValue();
//            for(DispachData dispachData : list){
//                log.info("Region : " + region);
//                log.info("FaceInfo : " + dispachData.toString());
//            }
//        }
    }


    /**
     * 增加车辆布控
     * @param id 布控Id
     * @param region 布控地区
     * @param car 布控车辆
     */
    public void addCar(String id, Long region, String car){
        log.info("Add car dispatch : id " + id + " , region " + region + " , car " + car);
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
        log.info("Add mac dispatch : id " + id + " , region " + region + " , mac " + mac);
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
        log.info("Add face dispatch : id " + id + " , region " + region + " , bitFeature " + bitFeature);
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
        log.info("Delete mac or car dispatch. id " + id);
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
        log.info("Delete face dispatch. id " + id);
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
        for(DispachData dispachData : list){
            if(index.intValue() == dispachData.getIndex().intValue()){
                return dispachData.getId();
            }
        }
        return list.get(index).getId();
    }

    public List<DispachData> getCarInfo(Long region){
        return carInfos.get(region);
    }

    public List<DispachData> getMacInfo(Long region){
        return macInfos.get(region);
    }

    public DispatchAlive getDispachAlive(String ipcId){
        return dispachAlives.get(ipcId);
    }

    public Set<float[]> getFeatures(String ipcId){
        return ipcToFeatures.get(ipcId);
    }

//    public static TableCache getInstance(){
//        if(tableCache == null){
//            tableCache = new TableCache();
//        }
//        return tableCache;
//    }
}
