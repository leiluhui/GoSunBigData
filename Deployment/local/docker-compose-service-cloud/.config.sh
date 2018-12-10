#!/bin/bash

cd `dirname $0`
BIN_DIR=`pwd`
ENV_FILE=${BIN_DIR}/.env
cd ..
LOCAL_DIR=`pwd`
SPARK_DIR=${LOCAL_DIR}/spark
SPARK_PROPERTIES=${SPARK_DIR}/sparkJob.properties

function config(){
    ## 修改kafka相关配置
    KAFKA_HOST=`grep "KAFKA_HOST" ${ENV_FILE}| cut -d "=" -f2`
    KAFKA_PORT=`grep "KAFKA_PORT" ${ENV_FILE}| cut -d "=" -f2`
    sed -i "s#job.faceObjectConsumer.broker.list=.*#job.faceObjectConsumer.broker.list=${KAFKA_HOST}:${KAFKA_PORT}#g" ${SPARK_PROPERTIES}
    sed -i "s#job.kafkaToTidb.kafka=.*#job.kafkaToTidb.kafka=${KAFKA_HOST}:${KAFKA_PORT}#g" ${SPARK_PROPERTIES}

    ## 修改zk相关配置
    ZK_HOST=`grep "ZK_HOST" ${ENV_FILE}| cut -d "=" -f2`
    ZK_PROT=`grep "ZK_PORT" ${ENV_FILE}| cut -d "=" -f2`
    sed -i "s#job.zkDirAndPort=.*#job.zkDirAndPort=${ZK_HOST}:${ZK_PROT}#g" ${SPARK_PROPERTIES}
    sed -i "s#job.kafkaToTidb.zookeeper=.*#job.kafkaToTidb.zookeeper=${ZK_HOST}:${ZK_PROT}#g" ${SPARK_PROPERTIES}

    ## 修改es相关配置
    ES_HOST=`grep "ES_HOST" ${ENV_FILE}| cut -d "=" -f2`
    ES_PORT=`grep "ES_PORT" ${ENV_FILE}| cut -d "=" -f2`
    sed -i "s#job.kafkaToParquet.esNodes=.*#job.kafkaToParquet.esNodes=${ES_HOST}#g" ${SPARK_PROPERTIES}
    sed -i "s#job.kafkaToParquet.esPort=.*#job.kafkaToParquet.esPort=${ES_PORT}#g" ${SPARK_PROPERTIES}

    ## 修改数据库相关配置
    DB_HOST=`grep "MYSQL_HOST" ${ENV_FILE}| cut -d "=" -f2`
    DB_PORT=`grep "MYSQL_PORT" ${ENV_FILE}| cut -d "=" -f2`
    sed -i "s#job.kafkaToTidb.jdbc.ip=.*#job.kafkaToTidb.jdbc.ip=${DB_HOST}#g" ${SPARK_PROPERTIES}
    sed -i "s#job.kafkaToTidb.jdbc.port=.*#job.kafkaToTidb.jdbc.port=${DB_PORT}#g" ${SPARK_PROPERTIES}
}

config
