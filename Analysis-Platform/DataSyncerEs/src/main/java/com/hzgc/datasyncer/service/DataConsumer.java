package com.hzgc.datasyncer.service;

import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.bean.PersonObject;
import com.hzgc.common.util.basic.StopWatch;
import com.hzgc.datasyncer.bean.EsCarObject;
import com.hzgc.datasyncer.bean.EsFaceObject;
import com.hzgc.datasyncer.bean.EsPersonObject;
import com.hzgc.datasyncer.dao.CarRepository;
import com.hzgc.datasyncer.dao.FaceRepository;
import com.hzgc.datasyncer.dao.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataConsumer {
    @Value("${kafka.topic.face}")
    private String faceTopic;

    @Value("${kafka.topic.person}")
    private String personTopic;

    @Value("${kafka.topic.car}")
    private String carTopic;

    @Autowired
    @SuppressWarnings("unused")
    private FaceRepository faceRepository;

    @Autowired
    @SuppressWarnings("unused")
    private PersonRepository personRepository;

    @Autowired
    @SuppressWarnings("unused")
    private CarRepository carRepository;

    @Autowired
    @SuppressWarnings("unused")
    private EsIndexHelper esIndexHelper;

    @Autowired
    @SuppressWarnings("unused")
    private EsBeanHelper esBeanHelper;

    @KafkaListener(id = "face-datasyncer", containerFactory = "kafkaListenerContainerFactory",
            topics = "${kafka.topic.face}")
    public void faceConsumerListener(List<ConsumerRecord<String, String>> consumerRecords) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("convert");
        List<FaceObject> objectList = esBeanHelper.getObjectList(consumerRecords, FaceObject.class);
        List<EsFaceObject> esFaceObjects = esBeanHelper.faceBeanConvert(objectList);
        stopWatch.stop();
        stopWatch.start("save");
        if (esFaceObjects != null && esFaceObjects.size() > 0) {
            faceRepository.saveAll(esFaceObjects);
            stopWatch.stop();
            log.info("Put data sucssefull, topic is:{}, receive number is:{}, save number is;{}, "
                            + stopWatch.getTaskInfo()[0].getTaskName() + " time is:{}, "
                            + stopWatch.getTaskInfo()[1].getTaskName() + " time is:{}",
                    consumerRecords.get(0).topic(), consumerRecords.size(), esFaceObjects.size(),
                    stopWatch.getTaskInfo()[0].getTimeMillis(), stopWatch.getTaskInfo()[1].getTimeMillis());
        }
    }


    @KafkaListener(id = "person-datasyncer", containerFactory = "kafkaListenerContainerFactory",
            topics = "${kafka.topic.person}")
    public void personConsumerListener(List<ConsumerRecord<String, String>> consumerRecords) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("convert");
        List<PersonObject> objectList = esBeanHelper.getObjectList(consumerRecords, PersonObject.class);
        List<EsPersonObject> esPersonObjects = esBeanHelper.personBeanConvert(objectList);
        stopWatch.stop();
        stopWatch.start("save");
        if (esPersonObjects != null && esPersonObjects.size() > 0) {
            personRepository.saveAll(esPersonObjects);
            stopWatch.stop();
            log.info("Put data sucssefull, topic is:{}, receive number is:{}, save number is;{}, "
                            + stopWatch.getTaskInfo()[0].getTaskName() + " time is:{}, "
                            + stopWatch.getTaskInfo()[1].getTaskName() + " time is:{}",
                    consumerRecords.get(0).topic(), consumerRecords.size(), esPersonObjects.size(),
                    stopWatch.getTaskInfo()[0].getTimeMillis(), stopWatch.getTaskInfo()[1].getTimeMillis());
        }
    }

    @KafkaListener(id = "car-datasyncer", containerFactory = "kafkaListenerContainerFactory",
            topics = "${kafka.topic.car}")
    public void carConsumerListener(List<ConsumerRecord<String, String>> consumerRecords) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("convert");
        List<CarObject> objectList = esBeanHelper.getObjectList(consumerRecords, CarObject.class);
        List<EsCarObject> esCarObjects = esBeanHelper.carBeanConvert(objectList);
        stopWatch.stop();
        stopWatch.start("save");
        if (esCarObjects != null && esCarObjects.size() > 0) {
            carRepository.saveAll(esCarObjects);
            stopWatch.stop();
            log.info("Put data sucssefull, topic is:{}, receive number is:{}, save number is;{}, "
                            + stopWatch.getTaskInfo()[0].getTaskName() + " time is:{}, "
                            + stopWatch.getTaskInfo()[1].getTaskName() + " time is:{}",
                    consumerRecords.get(0).topic(), consumerRecords.size(), esCarObjects.size(),
                    stopWatch.getTaskInfo()[0].getTimeMillis(), stopWatch.getTaskInfo()[1].getTimeMillis());
        }
    }
}