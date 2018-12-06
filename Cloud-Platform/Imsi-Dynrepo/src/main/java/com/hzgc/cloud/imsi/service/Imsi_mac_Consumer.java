package com.hzgc.cloud.imsi.service;

import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.cloud.imsi.dao.ImsiInfoMapper;
import com.hzgc.cloud.imsi.dao.MacInfoMapper;
import com.hzgc.cloud.imsi.model.MacInfo;
import com.hzgc.cloud.imsi.util.ImsiCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@Slf4j
public class Imsi_mac_Consumer {

    @Autowired
    private ImsiInfoMapper imsiInfoMapper;

    @Autowired
    private MacInfoMapper macInfoMapper;

    @Autowired
    private ImsiProducer imsiProducer;

    @Value(value = "${tag}")
    private String tag;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = {"imsi", "mac"})
    public void receiveMessage(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        if ("imsi".equals(topic)) {
            Optional<String> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                String message = kafkaMessage.get();
                log.info("Recevice imsi message is " + message);
                ImsiInfo imsiInfo = JacksonUtil.toObject(message, ImsiInfo.class);
                long savetime = imsiInfo.getSavetime();
                if ("1".equals(tag)) {
                    boolean b = ImsiCheck.checkImsi(imsiInfo.getImsi(), savetime);
                    if (b) {
                        imsiInfo.setTime(sdf.format(savetime));
                        String id = UuidUtil.getUuid();
                        imsiInfo.setId(id);
                        int i = imsiInfoMapper.insertSelective(imsiInfo);
                        if (i > 0) {
                            log.info("Insert imsi info is successful");
                            imsiProducer.sendMessage("PeoMan-IMSI",id,JacksonUtil.toJson(imsiInfo));
                        } else {
                            log.info("Insert imsi info is failed");
                        }
                    } else {
                        log.info("Now time is less than one hour time");
                    }
                }else {
                    imsiInfo.setTime(sdf.format(savetime));
                    String id = UuidUtil.getUuid();
                    imsiInfo.setId(id);
                    int i = imsiInfoMapper.insertSelective(imsiInfo);
                    if (i > 0) {
                        log.info("Insert imsi info is successful");
                        imsiProducer.sendMessage("PeoMan-IMSI",id,JacksonUtil.toJson(imsiInfo));
                    } else {
                        log.info("Insert imsi info is failed");
                    }
                }
            }
        }
        if ("mac".equals(topic)) {
            Optional<String> kafkaMessage = Optional.ofNullable(record.value());
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
