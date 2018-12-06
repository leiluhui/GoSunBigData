package com.hzgc.cluster.dispatch.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Producer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic , String payload) {
        try {
            log.info("Send message " + payload);
            kafkaTemplate.send(topic, payload);
            kafkaTemplate.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
//        log.info(kafkaTemplate.);
    }
}