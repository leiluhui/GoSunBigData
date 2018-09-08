#!/bin/bash
################################################################################
##opyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud stop facedispatch
## Description:  停止facedispatch服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x              ##用于调试

cd `dirname $0`
BIN_DIR=`pwd`                           ##bin目录地址
cd ..
FACE_DISPATCH_DIR=`pwd`                       ##face-dispatch目录地址
LIB_DIR=${FACE_DISPATCH_DIR}/lib              ##lib目录地址
CONF_DIR=${FACE_DISPATCH_DIR}/conf            ##conf目录地址
FACE_DISPATCH_JAR_NAME=`ls ${LIB_DIR} | grep ^face-dispatch-[0-9].[0-9].[0-9].jar$`
FACE_DISPATCH_JAR_PID=`jps | grep ${FACE_DISPATCH_JAR_NAME} | awk '{print $1}'`


#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名:stop_spring_cloud
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_springCloud()
{
    if [ -n "${FACE_DISPATCH_JAR_PID}" ];then
       echo "FaceDispatch service is exist, exit with 0, kill service now!!"
  	   ##杀掉进程
	   kill -9 ${FACE_DISPATCH_JAR_PID}
	   echo "stop service successfully!!"
	else
	   echo "FaceDispatch service is not start!!"
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
    stop_springCloud
}

main