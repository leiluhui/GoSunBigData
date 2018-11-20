#!/bin/bash

spark-submit \
--master local[*] \
--conf "spark.driver.extraJavaOptions=-Dkafka_broker=${KAFKA_BROKER} -Dzk_address=${ZK_ADDRESS} -Des_node=${ES_NODE}
-Dapp_name=FaceObjectConsumer -Dkafka_group=FaceObjectConsumerGroup -Dtime_interval=15 -Dstore_address='hdfs://hzgc/user/hive/warehouse/mid_table/'
-Dface_topic=face -Dperson_topic -Dcar_topic=car -Dzk_face_path='/parquet' -Dzk_person_path=/person -Dzk_car_path=/car -Des_port=9200" \
--class com.hzgc.cluster.spark.consumer.KafkaToParquet \
/spark.jar
