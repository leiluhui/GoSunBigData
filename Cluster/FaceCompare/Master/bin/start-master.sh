#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-master.sh
## Description: 启动Master
## Author:      wujiaqi
## Created:     2018-09-11
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
LOG_FILE=${LOG_DIR}/master.log                              ### log文件
CONF_DIR=${COMPARE_DIR}/conf                                ### conf目录
LIB_DIR=${COMPARE_DIR}/lib                                  ### lib目录
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata


if [ ! -d ${LOG_DIR} ]; then
    mkdir ${LOG_DIR}
fi


#####################################################################
# 函数名: master_protect
# 描述: master 启动的时候，为master添加定时检测服务是否挂掉脚本，保证master挂掉后可以重新拉起
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function master_protect()
{
    isExistProtest=$(grep master-protect /etc/crontab | wc -l)
    if [ "${isExistProtest=}" == "1" ];then
        echo "master_protect already exist"
    else
        echo "master_protect not exist, add master_protect into crontab"
        echo "* */1 * * * root sync;sh ${BIN_DIR}/master-protect.sh" >> /etc/crontab
        service crond restart
    fi
}

#####################################################################
# 函数名: start_master
# 描述: 启动master
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_master()
{
    nohup java -server -Xms1g -Xmx3g -classpath $CONF_DIR:$LIB_JARS com.hzgc.compare.FaceCompareMaster > ${LOG_FILE} 2>&1 &
    echo "start master ..."
}

#master_protect
start_master
