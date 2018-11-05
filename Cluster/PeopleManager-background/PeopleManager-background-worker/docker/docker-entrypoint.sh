#!/usr/bin/env bash


exec java -jar app.jar --spring.profiles.active=pro --spring.cloud.config.enabled=false --kafka.host=${KAFKA_HOST} --zookeeper.address=${ZK_ADDRESS} --compare.number=${COMPARE_NUMBER} --bit.threshold=${BIT_THRESHOLD} --float.threshold=${FLOAT_THRESHOLD} --float.new.threshold=${FLOAT_NEW_THRESHOLD} --float.compare.open=${FLOAT_COMPARE_OPEN} --mysql.host=${MYSQL_HOST} --kafka.host=${KAFKA_HOST}