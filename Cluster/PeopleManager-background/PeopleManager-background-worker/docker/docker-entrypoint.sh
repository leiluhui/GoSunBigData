#!/bin/bash

exec java -jar app.jar \
--spring.profiles.active=pro \
--spring.cloud.config.enabled=false \
--eureka.ip=${EUREKA_IP} \
--eureka.port=${EUREKA_PORT} \
--kafka.host=${KAFKA_HOST} \
--zookeeper.address=${ZOOKEEPER_HOST} \
--compare.number=${COMPARE_NUMBER} \
--bit.threshold=${BIT_THRESHOLD} \
--float.threshold=${FLOAT_THRESHOLD} \
--float.new.threshold=${FLOAT_NEW_THRESHOLD} \
--float.compare.open=${FLOAT_COMPARE_OPEN} \
--mysql.host=${MYSQL_HOST}