package com.hzgc.cluster.peoman.worker.service;

import com.google.gson.Gson;
import com.hzgc.cluster.peoman.worker.dao.FlagMapper;
import com.hzgc.cluster.peoman.worker.dao.PeopleRecognizeMapper;
import com.hzgc.cluster.peoman.worker.model.PeopleRecognize;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.common.util.rocketmq.RocketMQProducer;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
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
    private MemeoryCache memeoryCache;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleRecognizeMapper peopleRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private FlagMapper flagMapper;

    @Value("kafka.bootstrap.servers")
    @SuppressWarnings("unused")
    private String kafkaHost;

    @Value("kafka.fusion.topic")
    @SuppressWarnings("unused")
    private String fusionTopic;

    @Value("face.float.threshold")
    @SuppressWarnings("unused")
    private float floatThreshold;

    @Value("face.bit.threshold")
    @SuppressWarnings("unused")
    private float featureThreshold;

    @Value("face.float.compare.open")
    @SuppressWarnings("unused")
    private boolean isOpen;

    @Value("rocketmq.nameserver")
    @SuppressWarnings("unused")
    private String mqNameServer;

    @Value("rocketmq.topic.name")
    @SuppressWarnings("unused")
    private String mqTopicName;

    @Value("rocketmq.group.id")
    @SuppressWarnings("unused")
    private String mqGroupId;

    private String yearMonth = new SimpleDateFormat("yyyyMM").format(System.currentTimeMillis());
    private Map<Integer, String> indexUUID = new HashMap<>();
    private LinkedList<byte[]> bitFeatureList = new LinkedList<>();
    private LinkedList<float[]> floatFeatureList = new LinkedList<>();
    private KafkaProducer<String, String> producer;

    public PeopleCompare() {
        FaceFunction.init();
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 0);
        properties.put("linger.ms", 1);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        producer = new KafkaProducer<String, String>(properties);
    }

    public void comparePeople(FaceObject faceObject) {
        String currentYearMonth = new SimpleDateFormat("yyyyMM").format(System.currentTimeMillis());
        if(!currentYearMonth.equals(yearMonth)) {
            bitFeatureList.clear();
            floatFeatureList.clear();
            indexUUID.clear();
            yearMonth = currentYearMonth;
        }

        ComparePicture comparePicture = memeoryCache.comparePicture(faceObject.getAttribute());
        if(comparePicture != null) {
            addPeopleRecognize(faceObject, comparePicture);

            if(comparePicture.getFlagId() == 8) {
                MessageMq mesg = new MessageMq();
                Gson gson = new Gson();
                mesg.setName(comparePicture.getName());
                mesg.setTime(faceObject.getTimeStamp());
                mesg.setDevId(faceObject.getIpcId());
                RocketMQProducer producerMQ = RocketMQProducer.getInstance(mqNameServer, mqTopicName, mqGroupId);
                producerMQ.send(mqTopicName, "zdalarm", comparePicture.getPeopleId(), gson.toJson(mesg).getBytes(), null);
            }

            ProducerRecord<String, String> record = new ProducerRecord<>(fusionTopic, faceObject.getId(), JacksonUtil.toJson(faceObject));
            producer.send(record);
        } else {
            addNewPeopleRecognize(faceObject);
        }
    }

    public void addPeopleRecognize(FaceObject faceObject, ComparePicture comparePicture) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(faceObject.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PeopleRecognize peopleRecognize = new PeopleRecognize();
        peopleRecognize.setPeopleid(comparePicture.getPeopleId());
        peopleRecognize.setPictureid(comparePicture.getId());
        peopleRecognize.setDeviceid(faceObject.getIpcId());
        peopleRecognize.setCapturetime(date);
        peopleRecognize.setSurl(faceObject.getsFtpUrl());
        peopleRecognize.setBurl(faceObject.getbFtpUrl());
        peopleRecognize.setFlag(1);
        peopleRecognizeMapper.insert(peopleRecognize);
    }

    public void addNewPeopleRecognize(FaceObject faceObject) {
        HashMap resultMap = compareNewPeople(faceObject);
        PeopleRecognize peopleRecognize = new PeopleRecognize();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(faceObject.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(resultMap != null) {
            peopleRecognize.setPeopleid(indexUUID.get(resultMap.get("index")));
            peopleRecognize.setDeviceid(faceObject.getIpcId());
            peopleRecognize.setCapturetime(date);
            peopleRecognize.setSurl(faceObject.getsFtpUrl());
            peopleRecognize.setBurl(faceObject.getbFtpUrl());
            peopleRecognize.setFlag((Integer) resultMap.get("result"));
        }
    }

    /**
     *  新增人口比对
     * @param faceObject
     * @return index : 集合下标
     *         result : 新增：2, 完全新增(原图):10
     */
    public HashMap compareNewPeople(FaceObject faceObject) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        byte[] bitFeature = faceObject.getAttribute().getBitFeature();
        if (bitFeature != null && bitFeature.length > 0) {
            byte[][] queryList = new byte[1][];
            queryList[0] = bitFeature;
            ArrayList<CompareResult> compareResList =
                    FaceFunction.faceCompareBit(bitFeatureList.toArray(new byte[0][]), queryList, 1);
            CompareResult compareResult = compareResList.get(0);
            resultMap.put("index", Integer.valueOf(compareResult.getIndex()));
            ArrayList<FaceFeatureInfo> featureInfos = compareResult.getPictureInfoArrayList();
            FaceFeatureInfo faceFeatureInfo = featureInfos.get(0);
            if(isOpen) {
                float[] floatFeature = faceObject.getAttribute().getFeature();
                if(floatFeature != null && bitFeature.length > 0) {
                    float[][] queryFloatList = new float[1][];
                    queryFloatList[0] = floatFeature;
                    ArrayList<CompareResult> compareFloatResList =
                            FaceFunction.faceCompareFloat(floatFeatureList.toArray(new float[0][]), queryFloatList, 1);
                    FaceFeatureInfo faceFloatFeatureInfo = compareFloatResList.get(0).getPictureInfoArrayList().get(0);
                    if(faceFloatFeatureInfo.getScore() > floatThreshold) {
                        resultMap.put("result", 2);
                        return resultMap;
                    } else {
                        floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                        bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                        indexUUID.put(bitFeatureList.size(), faceObject.getId());
                        resultMap.put("result", 10);
                        return resultMap;
                    }
                } else {
                    return null;
                }
            } else {
                if(faceFeatureInfo.getScore() > featureThreshold) {
                    resultMap.put("result", 2);
                    return resultMap;
                } else {
                    bitFeatureList.addLast(faceObject.getAttribute().getBitFeature());
                    floatFeatureList.addLast(faceObject.getAttribute().getFeature());
                    indexUUID.put(bitFeatureList.size(), faceObject.getId());
                    resultMap.put("result", 10);
                    return resultMap;
                }
            }
        } else {
            return null;
        }
    }

}
