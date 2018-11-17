package com.hzgc.compare.worker.comsumer;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.compare.worker.common.tuple.Triplet;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Comsumer extends Thread{
    //    private static final Logger logger = LoggerFactory.getLogger(Comsumer.class);
    private static Logger log = Logger.getLogger(Comsumer.class);
    private MemoryCacheImpl memoryCache;
    private KafkaConsumer<String, String> comsumer;
    private boolean action;

    public Comsumer(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        Properties prop = new Properties();
        log.info("Bootstrap.servers is" + Config.KAFKA_BOOTSTRAP_SERVERS);
        prop.put("bootstrap.servers", Config.KAFKA_BOOTSTRAP_SERVERS);
        prop.put("group.id", Config.KAFKA_GROUP_ID);
        prop.put("key.deserializer", Config.KAFKA_DESERIALIZER);
        prop.put("value.deserializer", Config.KAFKA_DESERIALIZER);
        comsumer = new KafkaConsumer<>(prop);
        log.info("Kafka comsumer is init.");
        memoryCache = MemoryCacheImpl.getInstance();
    }

    private void stopComsumer(){
        action = false;
    }
    /**
     * 接收从kafka传来的数据
     */
    private void receiveAndSave(){
        comsumer.subscribe(Collections.singletonList(Config.KAFKA_TOPIC));
        log.info("Comsumer is started to accept kafka info.");
        log.info("Consumer param : " + Config.KAFKA_BOOTSTRAP_SERVERS);
        log.info("Consumer param : " + Config.KAFKA_GROUP_ID);
        log.info(Config.KAFKA_DESERIALIZER);
        log.info("Consumer param : " + Config.KAFKA_TOPIC);
        log.info("Consumer param : " + Config.KAFKA_MAXIMUM_TIME);
        action = true;
        while(action){
            ConsumerRecords<String, String> records =
                    comsumer.poll(Config.KAFKA_MAXIMUM_TIME);
//            List<FaceObject> objList = new ArrayList<>();
            List<Triplet<String, String, byte[]>> list = new ArrayList<>();
//            log.info("Consumer size : " + records.count());
            for(ConsumerRecord<String, String> record : records){
                FaceObject obj = JacksonUtil.toObject(record.value(), FaceObject.class);
                list.add(new Triplet<>(obj.getTimeStamp().split(" ")[0], obj.getId(), obj.getAttribute().getBitFeature()));
//                objList.add(obj);
                log.debug(record.value());
            }
//            memoryCache.addFaceObjects(objList);
            if(list.size() > 0) {
                memoryCache.addBuffer(list);
//                log.info("Push records from kafka to memory , the size is : " + list.size());
            }
        }
    }

    public void run() {
        receiveAndSave();
    }
}
