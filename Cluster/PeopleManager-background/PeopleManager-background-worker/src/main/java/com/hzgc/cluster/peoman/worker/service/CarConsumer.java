package com.hzgc.cluster.peoman.worker.service;

import com.hzgc.cluster.peoman.worker.dao.CarMapper;
import com.hzgc.cluster.peoman.worker.dao.RecognizeRecordMapper;
import com.hzgc.cluster.peoman.worker.model.Car;
import com.hzgc.cluster.peoman.worker.model.RecognizeRecord;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class CarConsumer implements Runnable{
    @Autowired
    @SuppressWarnings("unused")
    private CarMapper carMapper;

    @Autowired
    @SuppressWarnings("unused")
    private RecognizeRecordMapper recognizeRecordMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleCompare peopleCompare;

    @Value("${kafka.bootstrap.servers}")
    @SuppressWarnings("unused")
    private String kafkaHost;

    @Value("${kafka.car.topic}")
    @SuppressWarnings("unused")
    private String carTopic;

    @Value("${kafka.car.groupId}")
    @SuppressWarnings("unused")
    private String carGroupId;

    @Value("${kafka.inner.topic.polltime}")
    @SuppressWarnings("unused")
    private Long pollTime;

    private KafkaConsumer<String, String> consumer;

    public void initCarConsumer() {
        Properties properties = new Properties();
        properties.put("group.id", carGroupId);
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("value.deserializer", StringDeserializer.class.getName());
        properties.put("key.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(carTopic));
        log.info("topic="+carTopic+", groupid="+carGroupId+",kafkaHost="+kafkaHost);
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(pollTime);
            for (ConsumerRecord<String, String> record : records) {
//                log.info("====================kafka value="+record.value());
                if (record.value() != null && record.value().length() > 0) {
                    log.info("********************************CarCompare Start************************************");
                    CarObject carObject = JacksonUtil.toObject(record.value(), CarObject.class);
                    if (carObject != null) {
                        String plate_licence = carObject.getAttribute().getPlate_licence();
                        if (plate_licence != null) {
                            Car car = carMapper.selectByCar(plate_licence);
                            if (car != null && car.getPeopleid() != null) {
                                //识别车推送
                                peopleCompare.sendCarFocalRecord(carObject, car);

                                RecognizeRecord carRecognize = new RecognizeRecord();
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(carObject.getTimeStamp());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                CameraQueryDTO cameraQueryDTO = peopleCompare.getCameraQueryDTO(carObject.getIpcId());
                                if (cameraQueryDTO != null) {
                                    carRecognize.setCommunity(cameraQueryDTO.getCommunityId());
                                } else {
                                    log.info("getCameraQueryDTO data no community !!!, devId="+carObject.getIpcId());
                                }
                                carRecognize.setType(3);
                                carRecognize.setId(carObject.getId());
                                carRecognize.setPeopleid(car.getPeopleid());
                                carRecognize.setPlate(car.getCar());
                                carRecognize.setDeviceid(carObject.getIpcId());
                                carRecognize.setCapturetime(date);
                                carRecognize.setSurl(CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getsAbsolutePath()));
                                carRecognize.setBurl(CollectUrlUtil.toHttpPath(carObject.getHostname(), "2573", carObject.getbAbsolutePath()));
                                log.info("carRecognize value="+JacksonUtil.toJson(carRecognize));
                                try {
                                    recognizeRecordMapper.insertSelective(carRecognize);
                                } catch (Exception e) {
                                    log.info("CarCompare insert car recognize failed !!!");
                                    log.error(e.getMessage());
                                }
                            } else {
                                log.info("CarObject data not exist, plate_licence is={}, sFtpUrl={}",plate_licence, carObject.getsFtpUrl());
                            }
                        }
                    } else {
                        log.info("CarObject data value is null");
                    }
                    log.info("********************************CarCompare End************************************");
                }
            }
        }
    }

}