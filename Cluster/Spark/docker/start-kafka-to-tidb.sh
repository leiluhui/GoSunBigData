#!/bin/bash

spark-submit \
--master local[*] \
--conf "spark.driver.extraJavaOptions=-Dkafka_broker=${KAFKA_BROKER} -Dzk_address=${ZK_ADDRESS} -Djdbc_ip=${JDBC_IP}
-Ddriver=com.mysql.jdbc.Driver -Dapp_name=KafkaToTidb -Dcheck_point=/tmp/spark_out -Dgroup_id=1 -Dtime_out=10000 -Dtopic=imsi
-Djdbc_port=${JDBC_PORT}" \
--class com.hzgc.cluster.spark.consumer.KafkaToTidb \
/spark.jar