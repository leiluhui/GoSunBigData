package com.hzgc.service.imsi.service;

import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.dao.ImsiInfoMapper;
import com.hzgc.service.imsi.dao.MacInfoMapper;
import com.hzgc.service.imsi.model.ImsiInfo;
import com.hzgc.service.imsi.model.MacInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class Imsi_mac_Consumer {

    @Autowired
    private ImsiInfoMapper imsiInfoMapper;

    @Autowired
    private MacInfoMapper macInfoMapper;

    @KafkaListener(topics = {"imsi", "mac"})
    public void receiveMessage(ConsumerRecord <String, String> record) {
        String topic = record.topic();
        if ("imsi".equals(topic)) {
            Optional <String> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                String message = kafkaMessage.get();
                log.info("Recevice imsi message is " + message);
                ImsiInfo imsiInfo = JacksonUtil.toObject(message, ImsiInfo.class);
                int i = imsiInfoMapper.insertSelective(imsiInfo);
                if (i > 0) {
                    log.info("Insert imsi info is successful");
                } else {
                    log.info("Insert imsi info is failed");
                }
            }
        }
        if ("mac".equals(topic)) {
            Optional <String> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                String message = kafkaMessage.get();
                log.info("Receive mac message is " + message);
                MacInfo macInfo = JacksonUtil.toObject(message, MacInfo.class);
                int i = macInfoMapper.insertSelective(macInfo);
                if (i > 0) {
                    log.info("Insert mac info is successful");
                } else {
                    log.info("Insert mac info is failed");
                }
            }
        }
    }
}
