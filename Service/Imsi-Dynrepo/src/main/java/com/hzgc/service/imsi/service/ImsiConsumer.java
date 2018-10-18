package com.hzgc.service.imsi.service;

import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.dao.ImsiInfoMapper;
import com.hzgc.service.imsi.model.ImsiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ImsiConsumer {

    @Autowired
    private ImsiInfoMapper imsiInfoMapper;

    @KafkaListener(topics =  {"imsi"})
    public void receiveMessage(ConsumerRecord<String,String> record) {
        Optional <String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get();
            log.info("Recevice message is " + message);
            ImsiInfo imsiInfo = JacksonUtil.toObject(message, ImsiInfo.class);
            int i = imsiInfoMapper.insertSelective(imsiInfo);
            if (i > 0) {
                log.info("Insert imsi info is successful");
            } else {
                log.info("Insert imsi info is failed");
            }
        }
    }
}
