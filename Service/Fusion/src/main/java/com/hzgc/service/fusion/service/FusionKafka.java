package com.hzgc.service.fusion.service;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.fusion.dao.FusionImsiMapper;
import com.hzgc.service.fusion.model.FusionImsi;
import com.hzgc.service.fusion.model.ImsiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FusionKafka {

    @Autowired
    private ImsiInfo imsiInfo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FusionImsiMapper fusionImsiMapper;

    @KafkaListener(topics = {"PeoMan-Fusion"})
    public void listen(ConsumerRecord<String,String> record){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Optional<String> kafkaKey = Optional.ofNullable(record.key());
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaKey.isPresent() && kafkaMessage.isPresent()){
            String key = kafkaKey.get();
            String message = kafkaMessage.get();
            log.info("The message from the topic of PeoMan-Fusion'key is : " + key + " ,and the value is : " + message);
            FaceObject faceObject = JacksonUtil.toObject(message, FaceObject.class);
            String date = faceObject.getTimeStamp();
            try {
                long time = simpleDateFormat.parse(date).getTime();
                List<ImsiInfo> imsiList = restTemplate.getForObject("http://imsi-dynrepo/query_by_time?time=" + time, List.class);
                log.info("The imsiList is : " + imsiList);
                log.info("The imsiList's size is : " + imsiList.size());
                if (imsiList.size() > 0){
                    for (ImsiInfo imsi : imsiList){
                        FusionImsi fusionImsi = new FusionImsi();
                        fusionImsi.setPeopleid(key);
                        fusionImsi.setCommunity(Long.parseLong(imsi.getCellid()));
                        fusionImsi.setDeviceid(imsi.getSn());
                        fusionImsi.setReceivetime(new Date(imsi.getTime()));
                        fusionImsi.setImsi(imsi.getImsi());
                        int status = fusionImsiMapper.insertSelective(fusionImsi);
                        if (status != 1){
                            log.info("The Imsi is insert failed,please check it !!!");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
