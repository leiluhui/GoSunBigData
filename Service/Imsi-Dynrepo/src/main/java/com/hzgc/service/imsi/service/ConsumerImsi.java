package com.hzgc.service.imsi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
@Slf4j
public class ConsumerImsi implements Runnable {

    @Value(value = "${es.cluster.name}")
    private String clusterName;
    @Value("${es.hosts}")
    private String esHost;
    @Value("${es.cluster.port}")
    private String esPort;
    private String index = "com/hzgc/service/imsi";
    private String type = "recognize";
    private KafkaConsumer <String, String> consumer;
    private TransportClient esClient;

    public ConsumerImsi() {
        init();
//        esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    /*
     * kafka初始化
     * */
    public void init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "172.18.18.105:9092");
        properties.put("group.id", "consumer-tutorial");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer <>(properties);
        log.info("KafkaConsumer init is successful");
    }

    /*
     * 接收从kafka中传来的数据并进行es存储
     * */
    @Override
    public void run() {
        consumer.subscribe(Arrays.asList("topic"));
        log.info("Comsumer is started to accept kafka info");
        while (true) {
            ConsumerRecords <String, String> records = consumer.poll(1000);
            BulkRequestBuilder bulk = esClient.prepareBulk();
            for (ConsumerRecord <String, String> record : records) {
                //批量插入数据
                bulk.add(esClient.prepareIndex(index, type).setSource(record.value(), XContentType.JSON));
            }
            bulk.execute().actionGet();
        }
    }
}
