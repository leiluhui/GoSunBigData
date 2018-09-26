#!/bin/bash
##################################################################
## Copyright:      HZGOSUN Tech. Co, BigData
## Filename:       stop-workers.sh
## Desctiption:    停止所有的worker
## Author:         chenke
## Created:        2018-09-17
###################################################################

#set -x
#----------------------------------------------------------------#
#                             定义变量                           #
#----------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                                 ##bin目录地址
cd ..
PEOMAN_DIR=`pwd`                              ##peoman目录地址
PEOMAN_LOG_DIR=${PEOMAN_DIR}/log                ##conf目录地址
PEOMAN_LOG_FILE=${PEOMAN_LOG_DIR}/peoman.log
mkdir -p ${PEOMAN_LOG_DIR}
cd ..
CLUSTER_DIR=`pwd`                             ##Cluster目录地址
WORKER_DIR=${CLUSTER_DIR}/worker              ##worker目录地址
WORKER_BIN=${WORKER_DIR}/bin                  ##worker的bin目录地址
cd /opt/GoSunBigDataDeploy/project/
PROJECT_DIR=`pwd`                             ##project目录所在地址
PROJECT_CONF_DIR=${PROJECT_DIR}/conf          ##project的conf目录地址
PROJECT_FILE=${PROJECT_CONF_DIR}/project-conf.properties
PROJECT_BIN_DIR=${PROJECT_DIR}/bin            ##project的bin目录地址


#######################################################################
# 函数名： stop_workers
# 描述： 停止各个worker
# 参数： N/A
# 返回值： N/A
# 其他： N/A
########################################################################
function stop_workers()
{
     echo "" | tee -a ${PEOMAN_LOG_FILE}
     echo "**********************************" | tee -a ${PEOMAN_LOG_FILE}
     echo "" | tee -a ${PEOMAN_LOG_FILE}
     echo "开始执行停止每个节点上的worker......" | tee -a ${PEOMAN_LOG_FILE}

     WORKER_HOST_LISTS=$(grep worker_distrbution ${PROJECT_FILE} | cut -d '=' -f2)
     WORKER_HOST_ARRAY=(${WORKER_HOST_LISTS//;/})
     for hostname in ${WORKER_HOST_ARRAY[@]}
     do
       ssh root@${hostname} "sh ${WORKER_BIN}/stop-worker.sh"
       echo "${hostname} 上停止worker完毕.............." | tee -a ${PEOMAN_LOG_FILE}
     done
}

##############################################################################
# 函数名： main
# 描述： 脚本主要业务入口
# 参数： N/A
# 返回值： N/A
# 其他： N/A
##############################################################################
function main()
{
    stop_workers
}

#--------------------------------------------------------------------------#
#                                  执行流程                                #
#--------------------------------------------------------------------------#
##打印时间
echo ""
echo "=========================================================="
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

main

set +x

