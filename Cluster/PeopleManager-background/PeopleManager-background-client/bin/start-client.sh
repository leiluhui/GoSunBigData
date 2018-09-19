#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start jobClient
## Description: 启动 jobClient服务
## Author:      chenke
## Created:     2018-09-17
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
CLIENT_DIR=`pwd`                     ##client目录地址
LIB_DIR=${CLIENT_DIR}/lib                                          ##lib目录地址
CLIENT_JAR_NAME=`ls ${LIB_DIR}  | grep ^peoman-client-[0-9].[0-9].[0-9].jar$`          ##获取client的jar包名称
CLIENT_JAR=${LIB_DIR}/${CLIENT_JAR_NAME}                        ##获取jar包的全路径
CONF_DIR=${CLIENT_DIR}/conf                                        ##conf目录地址

#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_alarm
# 描述: 启动 springCloud alarm服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   CLIENT_PID=`jps | grep ${CLIENT_JAR_NAME} | awk '{print $1}'`
   if [  -n "${CLIENT_PID}" ];then
      echo "Client service already started!!"
   else
      nohup java -jar ${CLIENT_JAR} --spring.profiles.active=pro  \
      --spring.cloud.config.enabled=false  2>&1 &
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
    start_springCloud
}

main