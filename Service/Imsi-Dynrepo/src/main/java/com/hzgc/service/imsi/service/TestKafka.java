package com.hzgc.service.imsi.service;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.model.ImsiInfo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class TestKafka {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","172.18.18.100:9092");
        properties.put("acks","all");
        properties.put("retires",0);
        properties.put("batch.size",16384);
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //生产者发送消息
        String topic = "imsi";
        KafkaProducer <String, String> producer = new KafkaProducer <>(properties);
        for (int i = 1;i <= 5;i++) {
            ImsiInfo imsiInfo = new ImsiInfo();
            imsiInfo.setImei("123456789");
            imsiInfo.setImsi("999999999999");
            imsiInfo.setSn("123456789");
            imsiInfo.setSavetime(123456789L);
            ProducerRecord <String, String> msg = new ProducerRecord <>(topic, JacksonUtil.toJson(imsiInfo));
            Future <RecordMetadata> send = producer.send(msg);
            producer.flush();
            System.out.println(send.isDone());
        }
    }
}
