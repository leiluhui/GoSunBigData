#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start peoplemanager
## Description: 启动 peoplemanager服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
PEOPLEMANAGER_DIR=`pwd`                     ##peoplemanager目录地址
LIB_DIR=${PEOPLEMANAGER_DIR}/lib            ##lib目录地址
CONF_DIR=${PEOPLEMANAGER_DIR}/conf          ##conf目录地址
PEOPLEMANAGER_JAR_NAME=`ls ${LIB_DIR} | grep ^peoplemanager-[0-9].[0-9].[0-9].jar$`          ##获取peoplemanager的jar包名称
PEOPLEMANAGER_JAR=${LIB_DIR}/${PEOPLEMANAGER_JAR_NAME}                        ##获取jar包的全路径



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.191
EUREKA_PORT=9000
ES_HOST=172.18.18.100
KAFKA_HOST=172.18.18.100:9092
ZOOKEEPER_HOST=172.18.18.100


#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_peoplemanager
# 描述: 启动 springCloud peoplemanager服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   PEOPLEMANAGER_PID=`jps | grep ${PEOPLEMANAGER_JAR_NAME} | awk '{print $1}'`
   if [  -n "${PEOPLEMANAGER_PID}" ];then
      echo "PeopleManager service already started!!"
   else
      nohup java -jar ${PEOPLEMANAGER_JAR} --spring.profiles.active=pro \
       --eureka.ip=${EUREKA_IP} \
       --eureka.port=${EUREKA_PORT} \
       --spring.cloud.config.enabled=false \
       --kafka.host=${KAFKA_HOST} \
       --zookeeper.host=${ZOOKEEPER_HOST} \
       --es.host=${ES_HOST}  2>&1 &
   fi
}
#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud face服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function prepare_resource_file()
{
  cp ${CONF_DIR}/hbase-site.xml .
  jar -uf ${PEOPLEMANAGER_JAR} hbase-site.xml
  rm -rf hbase-site.xml
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
    prepare_resource_file
    start_springCloud
}

main