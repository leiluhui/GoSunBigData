#!/usr/bin/env bash
#/bin/bash

exec java -jar app.jar \
--zookeeper.host=${ZOOKEEPER_HOST} \
--es.host=${ES_HOST} \
--mysql.host=${MYSQL_HOST}