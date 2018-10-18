package com.hzgc.service.imsi.service;

import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.model.ImsiInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImsiProducer {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage() {
        ImsiInfo imsiInfo = new ImsiInfo();
        long l = System.currentTimeMillis();
        imsiInfo.setSavetime(l);
        kafkaTemplate.send("imsi", JacksonUtil.toJson(imsiInfo));
        log.info("消息已经发送");
    }
}
