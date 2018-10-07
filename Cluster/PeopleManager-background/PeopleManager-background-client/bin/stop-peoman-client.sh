#!/bin/bash
##################################################################
## Copyright:      HZGOSUN Tech. Co, BigData
## Filename:       stop-peoman-client.sh
## Desctiption:    停止jobClient
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
CLIENT_DIR=`pwd`                              ##client目录地址
CLIENT_LOG_DIR=${CLIENT_DIR}/log              ##client的log目录地址
CLIENT_LIB_DIR=${CLIENT_DIR}/lib              ##client的lib目录地址
CLIENT_LOG_FILE=${CLIENT_LOG_DIR}/client.log
CLIENT_JAR_NAME=`ls ${CLIENT_LIB_DIR} | grep ^peoman-client-[0-9].[0-9].jar$`
CLIENT_JAR_PID=`jps | grep ${CLIENT_JAR_NAME} | awk '{print $1}'`

#######################################################################
# 函数名： stop_worker
# 描述： 停止worker
# 参数： N/A
# 返回值： N/A
# 其他： N/A
########################################################################
function stop_client()
{
  if [ -n "${CLIENT_JAR_PID}" ]; then
     echo "Client is exist,exit with 0,kill service now!"
     ##杀死进程
     kill -9 ${CLIENT_JAR_PID}
     echo "Stop client successfully!!"
  else
     echo "Client is not start!!"
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
   stop_client
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