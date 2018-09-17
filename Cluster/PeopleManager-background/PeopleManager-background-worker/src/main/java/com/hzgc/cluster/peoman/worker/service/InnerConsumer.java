package com.hzgc.cluster.peoman.worker.service;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@Component
public class InnerConsumer implements Runnable {
    @Autowired
    private MemeoryCache memeoryCache;

    @Autowired
    private Environment environment;

    @Value("kafka.bootstrap.servers")
    private String kafkaHost;

    @Value("kafka.inner.group.id")
    private String groupId;

    @Value("kafka.inner.topic")
    private String innerTopic;

    @Value("kafka.inner.topic.polltime")
    private Long pollTime;

    private KafkaConsumer<String, String> consumer;

    public void initInnerConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("group.id", groupId);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(innerTopic));
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(pollTime);
            for (ConsumerRecord<String, String> record : records) {
            }
        }
    }
}
