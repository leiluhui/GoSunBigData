#!/bin/bash
########################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     UpdateRegionTable
## Description:  更新区域表
## Author:       chenke
## Created:      2018-08-14
########################################################################
#set -x

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
function update()
{
  java -cp ${LIB_DIR}/htrace-core-3.1.0-incubating.jar:${LIB_DIR}/netty-common-4.1.11.Final.jar:${LIB_DIR}/netty-transport-4.1.11.Final.jar:${LIB_DIR}/protobuf-java-2.5.0.jar:${LIB_DIR}/hbase-protocol-1.3.2.jar:${LIB_DIR}/zookeeper-3.4.10.jar:${LIB_DIR}/hadoop-auth-2.7.2.jar:${LIB_DIR}/commons-lang-2.6.jar:${LIB_DIR}/commons-configuration-1.6.jar:${LIB_DIR}/slf4j-api-1.7.16.jar:${LIB_DIR}/hbase-client-1.3.2.jar:${LIB_DIR}/commons-collections-3.2.2.jar:${LIB_DIR}/guava-11.0.2.jar:${LIB_DIR}/commons-logging-1.1.3.jar:${LIB_DIR}/hadoop-common-2.7.2.jar:${SPARK_JAR}:${LIB_DIR}/common-hbase-1.0.jar:${LIB_DIR}/log4j-1.2.17.jar:${LIB_DIR}/common-util-1.0.jar:${LIB_DIR}/hbase-common-1.3.2.jar com.hzgc.cluster.spark.clustering.UpdateRegion
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
   update
}

main
