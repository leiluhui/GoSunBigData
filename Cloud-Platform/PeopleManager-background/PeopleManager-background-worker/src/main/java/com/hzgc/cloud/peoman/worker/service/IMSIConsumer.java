package com.hzgc.cloud.peoman.worker.service;

import com.hzgc.cloud.peoman.worker.dao.IMSIMapper;
import com.hzgc.cloud.peoman.worker.dao.RecognizeRecordMapper;
import com.hzgc.cloud.peoman.worker.model.RecognizeRecord;
import com.hzgc.cloud.peoman.worker.model.IMSI;
import com.hzgc.common.service.imsi.ImsiInfo;
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
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Component
public class IMSIConsumer implements Runnable{
    @Autowired
    @SuppressWarnings("unused")
    private IMSIMapper imsiMapper;

    @Autowired
    @SuppressWarnings("unused")
    private RecognizeRecordMapper recognizeRecordMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleCompare peopleCompare;

    @Value("${kafka.bootstrap.servers}")
    @SuppressWarnings("unused")
    private String kafkaHost;

    @Value("${kafka.imsi.topic}")
    @SuppressWarnings("unused")
    private String imsiTopic;

    @Value("${kafka.imsi.groupId}")
    @SuppressWarnings("unused")
    private String imsiGroupId;

    @Value("${kafka.inner.topic.polltime}")
    @SuppressWarnings("unused")
    private Long pollTime;

    private KafkaConsumer<String, String> consumer;

    public void initIMSIConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("group.id", imsiGroupId);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(imsiTopic));
        log.info("topic="+imsiTopic+", groupid="+imsiGroupId+",kafkaHost="+kafkaHost);
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(pollTime);
            for (ConsumerRecord<String, String> record : records) {
//                log.info("====================kafka value="+record.value());
                if (record.value() != null && record.value().length() > 0) {
                    log.info("------------------------------------IMSICompare Start---------------------------------");
                    ImsiInfo imsiInfo = JacksonUtil.toObject(record.value(), ImsiInfo.class);
                    if (imsiInfo != null) {
                        String imsi = imsiInfo.getImsi();
                        if (imsi != null && imsi.length() == 15) {
                            IMSI imsiData = imsiMapper.selectByIMSI(imsi);
                            if (imsiData != null && imsiData.getPeopleid() != null) {
                                //imsi识别推送
                                peopleCompare.sendIMSIFocalRecord(imsiInfo, imsiData);

                                RecognizeRecord imsiRecognize = new RecognizeRecord();
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(imsiInfo.getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                               imsiRecognize.setType(2);
                               imsiRecognize.setId(imsiInfo.getId());
                               imsiRecognize.setPeopleid(imsiData.getPeopleid());
                               imsiRecognize.setCommunity(imsiInfo.getCommunityId());
                               imsiRecognize.setDeviceid(imsiInfo.getControlsn());
                               imsiRecognize.setCapturetime(date);
                               imsiRecognize.setImsi(imsiInfo.getImsi());
                                log.info("imsiRecognize value="+JacksonUtil.toJson(imsiRecognize));
                                try {
                                    recognizeRecordMapper.insertSelective(imsiRecognize);
                                } catch (Exception e) {
                                    log.info("IMSICompare insert imsi recognize failed !!!");
                                    log.error(e.getMessage());
                                }
                            } else {
                                log.info("ImsiInfo data not exist, imsi={}",imsi);
                            }
                        }
                    } else {
                        log.info("ImsiInfo data value is null");
                    }
                    log.info("------------------------------------IMSICompare End---------------------------------");
                }
            }
        }
    }

}