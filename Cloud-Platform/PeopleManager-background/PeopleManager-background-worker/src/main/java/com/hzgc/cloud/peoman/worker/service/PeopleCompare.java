package com.hzgc.cloud.peoman.worker.service;

import com.hzgc.cloud.peoman.worker.dao.InnerFeatureMapper;
import com.hzgc.cloud.peoman.worker.dao.PeopleMapper;
import com.hzgc.cloud.peoman.worker.dao.RecognizeRecordMapper;
import com.hzgc.cloud.peoman.worker.model.*;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class PeopleCompare {
    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private InnerService innerService;

    @Autowired
    @SuppressWarnings("unused")
    private MemeoryCache memeoryCache;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleMapper peopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private RecognizeRecordMapper recognizeRecordMapper;

    @Autowired
    @SuppressWarnings("unused")
    private InnerFeatureMapper innerFeatureMapper;

    @Value("${kafka.fusion.topic}")
    @SuppressWarnings("unused")
    private String fusionTopic;

    @Value("${kafka.focal.topic}")
    @SuppressWarnings("unused")
    private String focalTopic;

    @Value("${face.float.new.threshold}")
    @SuppressWarnings("unused")
    private float floatThreshold;

    @Value("${face.bit.threshold}")
    @SuppressWarnings("unused")
    private float featureThreshold;

    @Value("${filter.interval.time}")
    @SuppressWarnings("unused")
    private int filterTime;

    @Value("${update.inner.feature.time}")
    @SuppressWarnings("unused")
    private int updateInnerTime;

    @Value("${face.float.compare.open}")
    @SuppressWarnings("unused")
    private boolean isOpen;

    @Value("${face.compare.number}")
    @SuppressWarnings("unused")
    private int compareNumber;

    private String yearMonth = "197001";
    private long timeFlag = 0;
    private int innerFeatureNum=0;
    private Map<Integer, String> indexUUID = new HashMap<>();
    private Map<String, CameraQueryDTO> cameraInfos = new HashMap<>();
    private LinkedList<byte[]> bitFeatureList = new LinkedList<>();
    private LinkedList<float[]> floatFeatureList = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();
    private KafkaProducer<String, String> producer;

    public PeopleCompare(@Value("${kafka.bootstrap.servers}") String kafkaHost) {
        FaceFunction.init();
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 0);
        properties.put("linger.ms", 1);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(properties);
    }

    public void comparePeople(FaceObject faceObject) {
        String currentYearMonth = new SimpleDateFormat("yyyyMM").format(System.currentTimeMillis());
        if (yearMonth.equals("197001")) {
            log.info("PeopleComare init, currentYearMonth=" + currentYearMonth + ", yearMonth=" + yearMonth);
            yearMonth = currentYearMonth;
        } else if (!currentYearMonth.equals(yearMonth)) {
            log.info("PeopleComare clear, currentYearMonth=" + currentYearMonth + ", yearMonth=" + yearMonth);
            bitFeatureList.clear();
            floatFeatureList.clear();
            indexUUID.clear();
            innerFeatureMapper.deleteAll();
            innerFeatureNum = 0;
            yearMonth = currentYearMonth;
        }
        if ((System.currentTimeMillis()/1000 - timeFlag > updateInnerTime) && (indexUUID.size() > innerFeatureNum)) {
            log.info("current={}, timeFlag={}",System.currentTimeMillis()/1000, timeFlag);
            for (int i=innerFeatureNum; i<indexUUID.size(); i++) {
                InnerFeature innerFeature = new InnerFeature();
                innerFeature.setPeopleid(indexUUID.get(i));
                innerFeature.setFeature(FaceUtil.floatFeature2Base64Str(floatFeatureList.get(i)));
                innerFeature.setBitfeature(FaceUtil.bitFeautre2Base64Str(bitFeatureList.get(i)));
                log.info("----------------insert t_inner_feature, peopleid={}", innerFeature.getPeopleid());
                try {
                    innerFeatureMapper.insert(innerFeature);
                } catch (Exception e) {
                    log.info("insert t_inner_feature failed !!!");
                    e.printStackTrace();
                }
            }
            innerFeatureNum = indexUUID.size();
            timeFlag = System.currentTimeMillis()/1000;
        }

        if(cameraInfos.size() <= 0) {
            Map<String, CameraQueryDTO> cameraInfoByBatchIpc = platformService.getCameraInfoByBatchIpc(new ArrayList<>());
            if (cameraInfoByBatchIpc != null && cameraInfoByBatchIpc.size() > 0) {
                cameraInfos.putAll(cameraInfoByBatchIpc);
            } else {
                log.error("platformService getCameraInfoByBatchIpc all data is null or empty exit !!!");
                return;
            }
        }
        if(cameraInfos == null || cameraInfos.size() == 0) {
            log.error("PeopleCompare cameraInfos data is null or empty exit !!!");
            return;
        }

        CameraQueryDTO cameraInfo = cameraInfos.get(faceObject.getIpcId());
        Long communityId = null;
        if (cameraInfo == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(faceObject.getIpcId());
            Map<String, CameraQueryDTO> cameraInfoByIpc = platformService.getCameraInfoByBatchIpc(list);
            if (cameraInfoByIpc != null && cameraInfoByIpc.size() > 0) {
                CameraQueryDTO cameraIpc = cameraInfoByIpc.get(faceObject.getIpcId());
                if(cameraIpc != null) {
                    communityId = cameraIpc.getCommunityId();
                    cameraInfos.put(faceObject.getIpcId(), cameraIpc);
                } else {
                    log.error("cameraIpc data no community exit !!!, devId="+faceObject.getIpcId());
                    return;
                }
            } else {
                log.error("platformService getCameraInfoByBatchIpc data no community exit !!!, devId="+faceObject.getIpcId());
                return;
            }
        } else {
            communityId = cameraInfo.getCommunityId();
        }
        ComparePicture comparePicture = memeoryCache.comparePicture(faceObject.getAttribute());
        if (comparePicture != null) {
            addPeopleRecognize(faceObject, comparePicture, communityId);
            //重点人口推送
            if (comparePicture.getFlagId() != null && comparePicture.getFlagId() != 7) {
                sendFaceFocalRecord(faceObject, comparePicture, communityId);
            }
            //数据融合推送
            ProducerRecord<String, String> record = new ProducerRecord<>(fusionTopic, faceObject.getId(), JacksonUtil.toJson(faceObject));
            producer.send(record);
            producer.flush();
        } else {
            addNewPeopleRecognize(faceObject, communityId);
        }
    }

    public void addPeopleRecognize(FaceObject faceObject, ComparePicture comparePicture, Long communityId) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(faceObject.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        People people = new People();
        people.setId(comparePicture.getPeopleId());
        people.setLasttime(date);
        peopleMapper.updateByPrimaryKeySelective(people);

        RecognizeRecord peopleRecognize = new RecognizeRecord();
        peopleRecognize.setType(1);
        peopleRecognize.setId(faceObject.getId());
        peopleRecognize.setPeopleid(comparePicture.getPeopleId());
        peopleRecognize.setPictureid(comparePicture.getId());
        peopleRecognize.setCommunity(communityId);
        peopleRecognize.setDeviceid(faceObject.getIpcId());
        peopleRecognize.setCapturetime(date);
        peopleRecognize.setSurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath()));
        peopleRecognize.setBurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath()));
        peopleRecognize.setFlag(1);
        peopleRecognize.setSimilarity(comparePicture.getSimilarity());
        peopleRecognize.setFilterTime(filterTime);
        log.info("insert people recognize value=" + JacksonUtil.toJson(peopleRecognize));
        try {
            recognizeRecordMapper.insertUpdate(peopleRecognize);
        } catch (Exception e) {
            log.info("PeopelCompare insertUpdate people recognize failed !!!");
            log.error(e.getMessage());
        }
    }

    /**
     * 新增人口比对
     *
     * @param faceObject
     * @return index : 集合下标
     * flag : 识别标签(2 : 新增, 10 ： 完全新增(原图))
     */
    public void addNewPeopleRecognize(FaceObject faceObject, Long communityId) {
        CompareRes compareRes = compareNewPeople(faceObject);
        RecognizeRecord peopleRecognize = new RecognizeRecord();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(faceObject.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (compareRes != null) {
            peopleRecognize.setType(1);
            peopleRecognize.setId(faceObject.getId());
            peopleRecognize.setPeopleid(indexUUID.get(compareRes.getIndex()));
            peopleRecognize.setDeviceid(faceObject.getIpcId());
            peopleRecognize.setPictureid(-1L);
            peopleRecognize.setCapturetime(date);
            peopleRecognize.setSurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath()));
            peopleRecognize.setBurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath()));
            peopleRecognize.setCommunity(communityId);
            peopleRecognize.setFlag(compareRes.getFlag());
            peopleRecognize.setSimilarity(compareRes.getSimilarity());
            peopleRecognize.setFilterTime(filterTime);
            log.info("insert new add people recognize value=" + JacksonUtil.toJson(peopleRecognize));
            if(compareRes.getFlag() == 10) {
                try {
                    recognizeRecordMapper.insert(peopleRecognize);
                } catch (Exception e) {
                    bitFeatureList.removeLast();
                    floatFeatureList.removeLast();
                    indexUUID.remove(indexUUID.size() -1);
                    log.info("PeopelCompare flag=10 insert new add people recognize failed !!!");
                    log.error(e.getMessage());
                }
            } else {
                try {
                    recognizeRecordMapper.insert(peopleRecognize);
                } catch (Exception e) {
                    log.info("PeopelCompare flag=2 insert new add people recognize failed !!!");
                    log.error(e.getMessage());
                }
            }
        }
    }

    public CompareRes compareNewPeople(FaceObject faceObject) {
        CompareRes compareRes = new CompareRes();
        byte[] bitFeature = faceObject.getAttribute().getBitFeature();
        if (bitFeature != null && bitFeature.length > 0) {
            byte[][] queryList = new byte[1][];
            queryList[0] = bitFeature;
            ArrayList<CompareResult> compareResList =
                    FaceFunction.faceCompareBit(bitFeatureList.toArray(new byte[0][]), queryList, compareNumber);
            if (compareResList != null && compareResList.size() > 0) {
                CompareResult compareResult = compareResList.get(0);
                ArrayList<FaceFeatureInfo> featureInfos = compareResult.getPictureInfoArrayList();
                if (featureInfos != null && featureInfos.size() > 0) {
                    if (isOpen) {
                        float tempSim = 0;
                        int index = 0;
                        for (int i=0; i<featureInfos.size(); i++) {
                            FaceFeatureInfo faceFeatureInfo = featureInfos.get(i);
                            float[] floatFeature = floatFeatureList.get(faceFeatureInfo.getIndex());
                            if (floatFeature != null && floatFeature.length == 512 && faceObject.getAttribute().getFeature().length == 512) {
                                float sim = FaceUtil.featureCompare(floatFeature, faceObject.getAttribute().getFeature());
                                if (sim > tempSim) {
                                    tempSim = sim;
                                    index = faceFeatureInfo.getIndex();
                                }
                            }
                        }
                        if (tempSim >= this.floatThreshold) {
                            compareRes.setFlag(2);
                            compareRes.setIndex(index);
                            compareRes.setSimilarity(tempSim);
                            log.info("CompareNewPeople flag=2 Float sim={}", tempSim);
                            return compareRes;
                        } else {
                            bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                            indexUUID.put(indexUUID.size(), faceObject.getId());
                            floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                            compareRes.setFlag(10);
                            compareRes.setIndex(indexUUID.size() -1);
                            compareRes.setSimilarity(tempSim);
                            log.info("CompareNewPeople flag=10 Float sim={}", tempSim);
                            return compareRes;
                        }
                    } else {
                        FaceFeatureInfo temp = featureInfos.get(0);
                        for (int i=0; i<featureInfos.size(); i++) {
                            FaceFeatureInfo faceFeatureInfo = featureInfos.get(i);
                            if (faceFeatureInfo.getScore() > temp.getScore()) {
                                temp = faceFeatureInfo;
                            }
                        }
                        if (temp.getScore() >= featureThreshold / 100) {
                            compareRes.setFlag(2);
                            compareRes.setIndex(temp.getIndex());
                            compareRes.setSimilarity(temp.getScore()*100);
                            log.info("CompareNewPeople flag=2 Bit score={}", temp.getScore()*100);
                            return  compareRes;
                        } else {
                            floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                            bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                            indexUUID.put(indexUUID.size(), faceObject.getId());
                            compareRes.setFlag(10);
                            compareRes.setIndex(indexUUID.size() -1);
                            compareRes.setSimilarity(temp.getScore()*100);
                            log.info("CompareNewPeople flag=10 Bit score={}", temp.getScore()*100);
                            return compareRes;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                indexUUID.put(indexUUID.size(), faceObject.getId());
                compareRes.setFlag(10);
                compareRes.setIndex(indexUUID.size() -1);
                compareRes.setSimilarity(Float.valueOf("0.0"));
                log.info("----------CompareNewPeople flag=10 compareResList is null");
                return compareRes;
            }
        } else {
            return null;
        }
    }

    public CameraQueryDTO getCameraQueryDTO(String devId) {
        if (devId != null) {
            CameraQueryDTO cameraInfo = cameraInfos.get(devId);
            if (cameraInfo == null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(devId);
                Map<String, CameraQueryDTO> cameraInfoByIpc = platformService.getCameraInfoByBatchIpc(list);
                if (cameraInfoByIpc != null && cameraInfoByIpc.size() > 0) {
                    CameraQueryDTO cameraIpc = cameraInfoByIpc.get(devId);
                    if(cameraIpc != null) {
                        cameraInfos.put(devId, cameraIpc);
                        return cameraIpc;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return cameraInfo;
            }
        } else {
            return null;
        }
    }

    public void sendFaceFocalRecord(FaceObject faceObject, ComparePicture comparePicture, Long communityId) {
        List<People> peopleList = peopleMapper.selectByPrimaryKey(comparePicture.getPeopleId());
        if (peopleList != null && peopleList.size() > 0) {
            MessageMq mesg = new MessageMq();
            mesg.setFlag(1);
            mesg.setDevId(faceObject.getIpcId());
            mesg.setTime(faceObject.getTimeStamp());
            mesg.setName(peopleList.get(0).getName());
            mesg.setAge(peopleList.get(0).getAge());
            mesg.setSex(peopleList.get(0).getSex());
            mesg.setBirthplace(peopleList.get(0).getBirthplace());
            mesg.setIdcard(peopleList.get(0).getIdcard());
            mesg.setAddress(peopleList.get(0).getAddress());

            HashSet<String>  phones= new HashSet<>();
            HashSet<String>  cars= new HashSet<>();
            HashSet<String>  imsis= new HashSet<>();
            HashSet<Long>  pictureids= new HashSet<>();
            for (People people : peopleList) {
                if (people.getPhone() != null)
                    phones.add(people.getPhone());
                if (people.getCar() != null)
                    cars.add(people.getCar());
                if (people.getImsi() != null && people.getImsi().length() == 15) {
                    String s = Long.toString(Long.valueOf(people.getImsi()), 32).toUpperCase();
                    if (s !=null && s.length() == 10) {
                        String mac = "IM-" + s.substring(0, 2) + "-" + s.substring(2, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8) + "-" + s.substring(8, 10);
                        imsis.add(mac);
                    }
                }
                if (people.getPictureid() != null)
                    pictureids.add(people.getPictureid());
            }
            mesg.setPhone(new ArrayList<>(phones));
            mesg.setCar(new ArrayList<>(cars));
            mesg.setMac(new ArrayList<>(imsis));
            mesg.setPictureid(new ArrayList<>(pictureids));
            mesg.setCommuntidy(communityId);
            mesg.setSurl(innerService.httpHostNameToIp(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath())).getHttp_ip());
            mesg.setBurl(innerService.httpHostNameToIp(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath())).getHttp_ip());

            ProducerRecord<String, String> record = new ProducerRecord<>(focalTopic, JacksonUtil.toJson(mesg));
            Future<RecordMetadata> send = producer.send(record);
            producer.flush();
        }
    }

    public void sendCarFocalRecord(CarObject carObject, Car car) {
        List<People> peopleList = peopleMapper.selectByPrimaryKey(car.getPeopleid());
        if (peopleList != null && peopleList.size() > 0) {
            MessageMq mesg = new MessageMq();
            mesg.setFlag(2);
            mesg.setDevId(carObject.getIpcId());
            mesg.setTime(carObject.getTimeStamp());
            mesg.setName(peopleList.get(0).getName());
            mesg.setAge(peopleList.get(0).getAge());
            mesg.setSex(peopleList.get(0).getSex());
            mesg.setBirthplace(peopleList.get(0).getBirthplace());
            mesg.setIdcard(peopleList.get(0).getIdcard());
            mesg.setAddress(peopleList.get(0).getAddress());

            HashSet<String> phones= new HashSet<>();
            HashSet<String>  cars= new HashSet<>();
            HashSet<String>  imsis= new HashSet<>();
            HashSet<Long>  pictureids= new HashSet<>();
            cars.add(car.getCar());
            for (People people : peopleList) {
                if (people.getPhone() != null)
                    phones.add(people.getPhone());
                if (people.getImsi() != null && people.getImsi().length() == 15) {
                    String s = Long.toString(Long.valueOf(people.getImsi()), 32).toUpperCase();
                    if (s !=null && s.length() == 10) {
                        String mac = "IM-" + s.substring(0, 2) + "-" + s.substring(2, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8) + "-" + s.substring(8, 10);
                        imsis.add(mac);
                    }
                }
                if (people.getPictureid() != null)
                    pictureids.add(people.getPictureid());
            }
            mesg.setPhone(new ArrayList<>(phones));
            mesg.setCar(new ArrayList<>(cars));
            mesg.setMac(new ArrayList<>(imsis));
            mesg.setPictureid(new ArrayList<>(pictureids));
            CameraQueryDTO cameraQueryDTO = getCameraQueryDTO(carObject.getIpcId());
            if (cameraQueryDTO != null) {
                mesg.setCommuntidy(cameraQueryDTO.getCommunityId());
            } else {
                log.info("getCameraQueryDTO data no community !!!, devId="+carObject.getIpcId());
            }
            mesg.setSurl(innerService.httpHostNameToIp(CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getsAbsolutePath())).getHttp_ip());
            mesg.setBurl(innerService.httpHostNameToIp(CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getbAbsolutePath())).getHttp_ip());

            ProducerRecord<String, String> record = new ProducerRecord<>(focalTopic, JacksonUtil.toJson(mesg));
            Future<RecordMetadata> send = producer.send(record);
            producer.flush();
        }
    }

    public void sendIMSIFocalRecord(ImsiInfo imsiInfo, IMSI imsiData) {
        List<People> peopleList = peopleMapper.selectByPrimaryKey(imsiData.getPeopleid());
        if (peopleList != null && peopleList.size() > 0) {
            MessageMq mesg = new MessageMq();
            mesg.setFlag(3);
            mesg.setDevId(imsiInfo.getControlsn());
            mesg.setTime(imsiInfo.getTime());
            mesg.setName(peopleList.get(0).getName());
            mesg.setAge(peopleList.get(0).getAge());
            mesg.setSex(peopleList.get(0).getSex());
            mesg.setBirthplace(peopleList.get(0).getBirthplace());
            mesg.setIdcard(peopleList.get(0).getIdcard());
            mesg.setAddress(peopleList.get(0).getAddress());

            HashSet<String>  phones= new HashSet<>();
            HashSet<String>  cars= new HashSet<>();
            HashSet<String>  imsis= new HashSet<>();
            HashSet<Long>  pictureids= new HashSet<>();
            if (imsiData.getImsi() != null && imsiData.getImsi().length() == 15) {
                String s = Long.toString(Long.valueOf(imsiData.getImsi()), 32).toUpperCase();
                if (s !=null && s.length() == 10) {
                    String mac = "IM-" + s.substring(0, 2) + "-" + s.substring(2, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8) + "-" + s.substring(8, 10);
                    imsis.add(mac);
                }
            }
            for (People people : peopleList) {
                if (people.getPhone() != null)
                    phones.add(people.getPhone());
                if (people.getCar() != null)
                    cars.add(people.getCar());
                if (people.getPictureid() != null)
                    pictureids.add(people.getPictureid());
            }
            mesg.setPhone(new ArrayList<>(phones));
            mesg.setCar(new ArrayList<>(cars));
            mesg.setMac(new ArrayList<>(imsis));
            mesg.setPictureid(new ArrayList<>(pictureids));
            mesg.setCommuntidy(imsiInfo.getCommunityId());

            ProducerRecord<String, String> record = new ProducerRecord<>(focalTopic, JacksonUtil.toJson(mesg));
            Future<RecordMetadata> send = producer.send(record);
            producer.flush();
        }
    }

    void putData(List<InnerFeature> featureList) {
        try {
            lock.lock();
            for (InnerFeature innerFeature : featureList) {
                if (innerFeature.getPeopleid() != null && innerFeature.getFeature() != null && innerFeature.getBitfeature() != null) {
                    byte[] bitFeature = FaceUtil.base64Str2BitFeature(innerFeature.getBitfeature());
                    float[] feature = FaceUtil.base64Str2floatFeature(innerFeature.getFeature());
                    bitFeatureList.addLast(bitFeature);
                    floatFeatureList.addLast(feature);
                    indexUUID.put(indexUUID.size(), innerFeature.getPeopleid());
                    innerFeatureNum ++;
                    log.info("**********{}, {}", innerFeatureNum, innerFeature.getPeopleid());
                }
            }
        } finally {
            lock.unlock();
        }
    }

}
