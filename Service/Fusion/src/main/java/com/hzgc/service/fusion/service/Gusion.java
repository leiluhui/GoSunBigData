package com.hzgc.service.fusion.service;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.fusion.dao.FusionImsiMapper;
import com.hzgc.service.fusion.model.FusionImsi;
import com.hzgc.service.fusion.model.ImsiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class Gusion implements Runnable {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaHost;

    @Value("${kafka.fusion.group.id}")
    private String groupId;

    @Value("${kafka.fusion.topic}")
    private String fusionTopic;

    @Value("${kafka.fusion.topic.polltime}")
    @SuppressWarnings("unused")
    private String pollTime;

    @Autowired
    private ImsiInfo imsiInfo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FusionImsiMapper fusionImsiMapper;

    private KafkaConsumer<String, String> consumer;

    public void initConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(fusionTopic));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.parseLong(pollTime));
            for (ConsumerRecord<String, String> record : records) {
                FaceObject faceObject = JacksonUtil.toObject(record.value(), FaceObject.class);
                String date = faceObject.getTimeStamp();
                try {
                    long time = simpleDateFormat.parse(date).getTime();
                    List<ImsiInfo> imsiList = restTemplate.getForObject("http://imsi-dynrepo/query_by_time?time=" + time, List.class);
                    if (imsiList.size() > 0){
                        for (ImsiInfo imsi : imsiList){
                            FusionImsi fusionImsi = new FusionImsi();
                            fusionImsi.setPeopleid(record.key());
                            fusionImsi.setDeviceid(imsi.getSn());
                            fusionImsi.setReceivetime(new Date(imsi.getTime()));
                            fusionImsi.setImsi(imsi.getImsi());
                            int status = fusionImsiMapper.insertSelective(fusionImsi);
                            if (status != 1){
                                log.info("The Imsi is insert failed,please check it !!!");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
