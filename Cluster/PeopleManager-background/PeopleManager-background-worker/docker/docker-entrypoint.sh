#!/bin/bash

exec java -jar app.jar \
--spring.profiles.active=pro \
--spring.cloud.config.enabled=false \
--kafka.host=${KAFKA_HOST} \
--zookeeper.address=${ZOOKEEPER_HOST} \
--compare.number=${COMPARE_NUMBER} \
--filter.interval.time=${FILTER_INTERVAL_TIME} \
--bit.threshold=${BIT_THRESHOLD} \
--float.threshold=${FLOAT_THRESHOLD} \
--float.new.threshold=${FLOAT_NEW_THRESHOLD} \
--float.compare.open=${FLOAT_COMPARE_OPEN} \
--sharpness.open=${SHARPNESS_OPEN} \
--mysql.host=${MYSQL_HOST} \
--eureka.ip=${EUREKA_IP} \
--eureka.port=${EUREKA_PORT} \
--mysql.username=${MYSQL_USERNAME} \
--mysql.password=${MYSQL_PASSWORD}