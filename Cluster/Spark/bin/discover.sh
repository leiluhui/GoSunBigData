#!/bin/bash
########################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     discover
## Description:  更新区域表
## Author:       chenke
## Created:      2018-08-14
########################################################################
set -x

cd `dirname $0`
BIN_DIR=`pwd`               ##bin目录地址
cd ..
SPARK_DIR=`pwd`     ##spark模块
LIB_DIR=${SPARK_DIR}/lib  ##lib目录地址
CONF_DIR=${SPARK_DIR}/conf  ##conf目录地址
SPARK_JAR_NAME=`ls ${LIB_DIR} | grep ^spark-[0-9].[0-9].[0-9].jar$`   ##获取peoplemanager的jar包名称
SPARK_JAR=${LIB_DIR}/${SPARK_JAR_NAME}            ##获取jar包的全路径

#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: update
# 描述: 修改region表
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function discover()
{
  java -cp ${LIB_DIR}/common-util-1.0.jar:${LIB_DIR}/log4j-1.2.17.jar:${SPARK_JAR} com.hzgc.cluster.spark.clustering.Discover
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
   discover
}

main