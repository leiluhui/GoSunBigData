#!/bin/bash

exec java -jar app.jar \
--spring.profiles.active=pro \
--spring.cloud.config.enabled=false \
--eureka.ip=${EUREKA_IP:-NULL} \
--eureka.port=${EUREKA_PORT:-NULL} \
--es.host=${ES_HOST} \
--kafka.host=${KAFKA_HOST} \
--redis.ip=${REDIS_IP} \
--redis.port=${REDIS_PORT}
