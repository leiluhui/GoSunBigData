//package com.hzgc.service.imsi.service;
//
//import com.alibaba.fastjson.JSON;
//import com.hzgc.common.util.json.JacksonUtil;
//import com.hzgc.service.imsi.dao.ImsiInfoMapper;
//import com.hzgc.service.imsi.model.ImsiInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//import java.util.Properties;
//
//@Component
//@Slf4j
//public class ConsumerImsi implements Runnable {
//
//    private KafkaConsumer <String, String> consumer;
//
//    @Autowired
//    ImsiInfoMapper imsiInfoMapper;
//
//    public ConsumerImsi(@Value("${bootstrap.servers}") String address) {
//        Properties properties = new Properties();
//        properties.put("group.id", "0");
//        properties.put("bootstrap.servers", address);
//        properties.put("enable.auto.commit", "true");
//        properties.put("auto.commit.interval.ms", "1000");
//        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        consumer = new KafkaConsumer <>(properties);
//        log.info("KafkaConsumer init is successful");
//    }
//
//    /*
//     * 接收从kafka中传来的数据并进行TIDB存储
//     * */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void run() {
//        consumer.subscribe(Collections.singletonList("imsi"));
//        log.info("Comsumer is started to accept kafka info");
//        while (true) {
//            ConsumerRecords <String, String> records = consumer.poll(10000);
//            for (ConsumerRecord <String, String> record : records) {
//                ImsiInfo imsiInfo = JacksonUtil.toObject(record.value(),ImsiInfo.class);
//                int i = imsiInfoMapper.insertSelective(imsiInfo);
//                if (i > 0) {
//                    log.info("Insert imsi info is successful");
//                } else {
//                    log.info("Insert imsi info is failed, record is " + record.value());
//                }
//            }
//        }
//    }
//}
