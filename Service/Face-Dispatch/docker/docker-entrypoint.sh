#!/usr/bin/env bash
#/bin/bash

exec java -jar app.jar --spring.profiles.active=pro --spring.cloud.config.enabled=false --eureka.ip=${EUREKA_IP} --eureka.port=${EUREKA_PORT} --bootstrap.servers=${KAFKA_HOST} --mysql.host=${MYSQL_HOST}