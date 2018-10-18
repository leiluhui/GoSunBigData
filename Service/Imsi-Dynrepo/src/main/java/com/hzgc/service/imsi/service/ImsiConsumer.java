package com.hzgc.service.imsi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ImsiConsumer {

    @KafkaListener(topics =  {"imsi"})
    public void receiveMessage(ConsumerRecord<String,String> record) {
        Optional <String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            log.info("--------message---------" + message);
        }
    }
}
