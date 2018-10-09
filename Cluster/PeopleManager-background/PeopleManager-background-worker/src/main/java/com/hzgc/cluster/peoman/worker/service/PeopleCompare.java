package com.hzgc.cluster.peoman.worker.service;

import com.google.gson.Gson;
import com.hzgc.cluster.peoman.worker.dao.FlagMapper;
import com.hzgc.cluster.peoman.worker.dao.PeopleMapper;
import com.hzgc.cluster.peoman.worker.dao.PeopleRecognizeMapper;
import com.hzgc.cluster.peoman.worker.model.People;
import com.hzgc.cluster.peoman.worker.model.PeopleRecognize;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.common.util.rocketmq.RocketMQProducer;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class PeopleCompare {
    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private MemeoryCache memeoryCache;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleMapper peopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleRecognizeMapper peopleRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private FlagMapper flagMapper;

    @Value("${kafka.fusion.topic}")
    @SuppressWarnings("unused")
    private String fusionTopic;

    @Value("${face.float.threshold}")
    @SuppressWarnings("unused")
    private float floatThreshold;

    @Value("${face.bit.threshold}")
    @SuppressWarnings("unused")
    private float featureThreshold;

    @Value("${face.float.compare.open}")
    @SuppressWarnings("unused")
    private boolean isOpen;

    @Value("${rocketmq.nameserver}")
    @SuppressWarnings("unused")
    private String mqNameServer;

    @Value("${rocketmq.topic.name}")
    @SuppressWarnings("unused")
    private String mqTopicName;

    @Value("${rocketmq.group.id}")
    @SuppressWarnings("unused")
    private String mqGroupId;

    private String yearMonth = "";
    private Map<Integer, String> indexUUID = new HashMap<>();
    private Map<String, CameraQueryDTO> cameraInfos = new HashMap<>();
    private LinkedList<byte[]> bitFeatureList = new LinkedList<>();
    private LinkedList<float[]> floatFeatureList = new LinkedList<>();
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
        if (!currentYearMonth.equals(yearMonth)) {
            log.info("PeopleComare init, currentYearMonth=" + currentYearMonth + ", yearMonth=" + yearMonth);
            bitFeatureList.clear();
            floatFeatureList.clear();
            indexUUID.clear();
            yearMonth = currentYearMonth;
        }
        if(cameraInfos.size() <= 0) {
            Map<String, CameraQueryDTO> cameraInfoByBatchIpc = platformService.getCameraInfoByBatchIpc(new ArrayList<>());
            cameraInfos.putAll(cameraInfoByBatchIpc);
        }
        if(cameraInfos == null || cameraInfos.size() == 0) {
            log.error("PeopleCompare getCameraInfoByBatchIpc data is null or empty exit !!!");
            return;
        }

        CameraQueryDTO cameraInfo = cameraInfos.get(faceObject.getIpcId());
        Long communityId = null;
        if (cameraInfo == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(faceObject.getIpcId());
            Map<String, CameraQueryDTO> cameraInfoByIpc = platformService.getCameraInfoByBatchIpc(list);
            CameraQueryDTO cameraIpc = cameraInfoByIpc.get(faceObject.getIpcId());
            if(cameraIpc != null) {
                communityId = cameraIpc.getCommunityId();
                cameraInfos.put(faceObject.getIpcId(), cameraIpc);
            } else {
                log.info("PeopleCompare getCameraInfoByBatchIpc data no community exit !!!, devId="+faceObject.getIpcId());
                return;
            }
        } else {
            communityId = cameraInfo.getCommunityId();
        }
        ComparePicture comparePicture = memeoryCache.comparePicture(faceObject.getAttribute());
        if (comparePicture != null) {
            addPeopleRecognize(faceObject, comparePicture, communityId);

            if (comparePicture.getFlagId() == 8) {
                MessageMq mesg = new MessageMq();
                Gson gson = new Gson();
                mesg.setName(comparePicture.getName());
                mesg.setTime(faceObject.getTimeStamp());
                mesg.setDevId(faceObject.getIpcId());
                RocketMQProducer producerMQ = RocketMQProducer.getInstance(mqNameServer, mqTopicName, mqGroupId);
                SendResult sendResult = producerMQ.send(mqTopicName, "ZD-Message", comparePicture.getPeopleId(), gson.toJson(mesg).getBytes(), null);
            }
            ProducerRecord<String, String> record = new ProducerRecord<>(fusionTopic, faceObject.getId(), JacksonUtil.toJson(faceObject));
            producer.send(record);
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

        PeopleRecognize peopleRecognize = new PeopleRecognize();
        peopleRecognize.setId(faceObject.getId());
        peopleRecognize.setPeopleid(comparePicture.getPeopleId());
        peopleRecognize.setPictureid(comparePicture.getId());
        peopleRecognize.setCommunity(communityId);
        peopleRecognize.setDeviceid(faceObject.getIpcId());
        peopleRecognize.setCapturetime(date);
        peopleRecognize.setSurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath()));
        peopleRecognize.setBurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath()));
        peopleRecognize.setFlag(1);
        log.info("insert people recognize value=" + JacksonUtil.toJson(peopleRecognize));
        peopleRecognizeMapper.insertSelective(peopleRecognize);
    }

    public void addNewPeopleRecognize(FaceObject faceObject, Long communityId) {
        HashMap resultMap = compareNewPeople(faceObject);
        PeopleRecognize peopleRecognize = new PeopleRecognize();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(faceObject.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (resultMap != null) {
            peopleRecognize.setId(indexUUID.get(resultMap.get("index")));
            peopleRecognize.setPeopleid(indexUUID.get(resultMap.get("index")));
            peopleRecognize.setDeviceid(faceObject.getIpcId());
            peopleRecognize.setPictureid(-1L);
            peopleRecognize.setCapturetime(date);
            peopleRecognize.setSurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getsAbsolutePath()));
            peopleRecognize.setBurl(CollectUrlUtil.toHttpPath(faceObject.getHostname(), "2573", faceObject.getbAbsolutePath()));
            peopleRecognize.setCommunity(communityId);
            peopleRecognize.setFlag((Integer) resultMap.get("flag"));
            log.info("insert new add people recognize value=" + JacksonUtil.toJson(peopleRecognize));
            peopleRecognizeMapper.insertSelective(peopleRecognize);
        }
    }

    /**
     * 新增人口比对
     *
     * @param faceObject
     * @return index : 集合下标
     * flag : 识别标签(2 : 新增, 10 ： 完全新增(原图))
     */
    public HashMap compareNewPeople(FaceObject faceObject) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        byte[] bitFeature = faceObject.getAttribute().getBitFeature();
        if (bitFeature != null && bitFeature.length > 0) {
            byte[][] queryList = new byte[1][];
            queryList[0] = bitFeature;
            ArrayList<CompareResult> compareResList =
                    FaceFunction.faceCompareBit(bitFeatureList.toArray(new byte[0][]), queryList, 1);
            if (compareResList == null) {
                bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                indexUUID.put(indexUUID.size(), faceObject.getId());
                resultMap.put("flag", 10);
                resultMap.put("index", indexUUID.size() - 1);
                return resultMap;
            }
            CompareResult compareResult = compareResList.get(0);
            ArrayList<FaceFeatureInfo> featureInfos = compareResult.getPictureInfoArrayList();
            FaceFeatureInfo faceFeatureInfo = featureInfos.get(0);
            if (isOpen && (faceFeatureInfo.getScore() >= featureThreshold / 100.0)) {
                float[] floatFeature = floatFeatureList.get(faceFeatureInfo.getIndex());
                if (floatFeature != null && floatFeature.length == 512 && faceObject.getAttribute().getFeature().length == 512) {
                    float sim = FaceUtil.featureCompare(floatFeature, faceObject.getAttribute().getFeature());
                    if (sim >= this.floatThreshold) {
                        resultMap.put("flag", 2);
                        resultMap.put("index", faceFeatureInfo.getIndex());
                        return resultMap;
                    } else {
                        bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                        indexUUID.put(indexUUID.size(), faceObject.getId());
                        floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                        resultMap.put("flag", 10);
                        resultMap.put("index", indexUUID.size() - 1);
                        return resultMap;
                    }
                } else {
                    return null;
                }

            } else {
                if (faceFeatureInfo.getScore() >= featureThreshold / 100) {
                    resultMap.put("flag", 2);
                    resultMap.put("index", Integer.valueOf(compareResult.getIndex()));
                    return resultMap;
                } else {
                    floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                    bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                    indexUUID.put(indexUUID.size(), faceObject.getId());
                    resultMap.put("flag", 10);
                    resultMap.put("index", indexUUID.size() - 1);
                    return resultMap;
                }
            }
        } else {
            return null;
        }
    }
}
