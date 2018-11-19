#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-worker.sh
## Description: 启动FaceCompareWorker
## Author:      wujiaqi
## Created:     2018-01-08
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                               ### bin目录
cd ..
COMPARE_DIR=`pwd`                                           ### compare目录
LOG_DIR=${COMPARE_DIR}/log                                  ### log目录
LOG_FILE=${LOG_DIR}/worker.log                              ### log文件
CONF_DIR=${COMPARE_DIR}/conf                                ### conf目录
LIB_DIR=${COMPARE_DIR}/lib                                  ### lib目录
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

WORK_ID=`cat /proc/sys/kernel/random/uuid`
WORK_ID=${WORK_ID%%-*}

LOG_FILE_STDERR=${LOG_DIR}/${WORK_ID}/stderr.log

if [ ! -d ${LOG_DIR} ]; then
    mkdir ${LOG_DIR}
fi

if [ ! -d ${LOG_DIR}/${WORK_ID} ]; then
    mkdir ${LOG_DIR}/${WORK_ID}
fi

#####################################################################
# 函数名: start_worker
# 描述: 启动worker
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_worker()
{

    echo "To Start FaceCompareWorker ${WORK_ID}"
    nohup java -server -Dkafka.servers=${KAFKA_SERVERS} -Dzookeeper.address=${ZOOKEEPER_ADDRESS} -Des.hosts=${ES_HOSTS} -Dworker.id=${WORK_ID} -Xms1g -Xmx4g -classpath $CONF_DIR:$LIB_JARS com.hzgc.compare.worker.FaceCompareWorker 2>&1 > $LOG_FILE_STDERR
    echo "start FaceCompareWorker ..."
}

start_worker