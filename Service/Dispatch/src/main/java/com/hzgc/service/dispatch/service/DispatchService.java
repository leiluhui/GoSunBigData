package com.hzgc.service.dispatch.service;

import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.dispatch.dao.DispatchMapper;
import com.hzgc.service.dispatch.dao.DispatchRecognizeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DispatchService {
    @Autowired
    @SuppressWarnings("unused")
    private DispatchMapper dispatchMapper;

    @Autowired
    @SuppressWarnings("unused")
    private DispatchRecognizeMapper dispatchRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    //Spring-kafka-template
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${dispatch.kafka.topic}")
    @NotNull
    @SuppressWarnings("unused")
    private String kafkaTopic;

    private static final String ADD = "ADD";

    private static final String DELETE = "DELETE";

    private static final String UPDATE = "UPDATE";

    private static final String IMPORT = "IMPORT";

    private void sendKafka(String key, Object data){
        try {
            ListenableFuture<SendResult<String, String>> resultFuture =
                    kafkaTemplate.send(kafkaTopic, key, JacksonUtil.toJson(data));
            RecordMetadata metaData = resultFuture.get().getRecordMetadata();
            ProducerRecord<String, String> producerRecord = resultFuture.get().getProducerRecord();
            if (metaData != null) {
                log.info("Send Kafka successfully! message:[topic:{}, key:{}, data:{}]",
                        metaData.topic(), key ,JacksonUtil.toJson(data));
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }
}
