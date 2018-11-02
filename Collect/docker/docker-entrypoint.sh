#/bin/bash

java -jar /app.jar \
--spring.profiles.active=pro \
--spring.cloud.config.enabled=false \
--zk.host=${ZOOKEEPER_HOST} \
--ftp.ip=${HOST_IP} \
--detector.number=${DETECTOR_NUMBER} \
--kafka.host=${KAFKA_HOST} \
--seemmo.url=${SEEMMO_URL} \
--home.dir=${HOME_DIR} \
--backup.dir=${BACKUP_DIR} \
--detector.enable=${DETECTOR_ENABLE} \
--eureka.ip=${EUREKA_IP} \
--eureka.port=${EUREKA_PORT}