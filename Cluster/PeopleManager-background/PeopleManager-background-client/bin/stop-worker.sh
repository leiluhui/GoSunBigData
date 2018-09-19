#!/bin/bash
##################################################################
## Copyright:      HZGOSUN Tech. Co, BigData
## Filename:       stop-worker.sh
## Desctiption:    停止worker
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
WORKER_DIR=`pwd`                              ##worker目录地址
WORKER_LOG_DIR=${WORKER_DIR}/log              ##worker的log目录地址
WORKER_LIB_DIR=${WORKER_DIR}/lib              ##worker的lib目录地址
WORKER_LOG_FILE=${WORKER_LOG_DIR}/worker.log
WORKER_JAR_NAME=`ls ${WORKER_LIB_DIR} | grep ^worker-[0-9].[0-9].[0-9].jar$`
WORKER_JAR_PID=`jps | grep ${WORKER_JAR_NAME} | awk '{print $1}'`

#######################################################################
# 函数名： stop_worker
# 描述： 停止worker
# 参数： N/A
# 返回值： N/A
# 其他： N/A
########################################################################
function stop_worker()
{
  if [ -n "${WORKER_JAR_PID}" ]; then
     echo "Worker is exist,exit with 0,kill service now!"
     ##杀死进程
     kill -9 ${WORKER_JAR_PID}
     echo "Stop worker successfully!!"
  else
     echo "Worker is not start!!"
  fi
}
#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
   stop_worker
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