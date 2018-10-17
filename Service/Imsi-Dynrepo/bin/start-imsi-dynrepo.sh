#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start imsi
## Description: 启动imsi服务提取特征
## Author:      yansen
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`    ##bin目录地址
cd ..
HOME_DIR=`pwd`    ##host目录地址
LIB_DIR=${HOME_DIR}/lib
IMSI_DYNREPO_JAR_NAME=`ls ${LIB_DIR} | grep ^imsi-dynrepo-[0-9].[0-9].[0-9].jar$`
IMSI_DYNREPO_JAR=${LIB_DIR}/${IMSI_DYNREPO_JAR_NAME}
CONF_DIR=${HOME_DIR}/conf

#---------------------------------------------------------------------#
#                          springcloud配置参数                        #
#---------------------------------------------------------------------#
EUREKA_IP=172.18.18.40     ##注册中心的ip地址
EUREKA_PORT=9000
ES_HOST=172.18.18.105
ZOOKEEPER_HOST=172.18.18.105:2181
MYSQL_HOST=172.18.18.105:3306
MYSQL_USERNAME=
MYSQL_PASSWORD=
QUERY_TIME=30
BOOTSTRAP_SERVERS=172.18.18.100:9092


#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud imsi服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   IMSI_DYNREPO_PID=`jps | grep ${IMSI_DYNREPO_JAR_NAME} | awk '{print $1}'`
   if [ -n "${IMSI_DYNREPO_PID}" ];then
       echo "imsi service already started"
   else
       nohup java -jar ${IMSI_DYNREPO_JAR} --spring.profiles.active=pro  \
       --eureka.ip=${EUREKA_IP} \
       --eureka.port=${EUREKA_PORT} \
       --spring.cloud.config.enabled=false \
       --mysql.host=${MYSQL_HOST} \
       --mysql.username=${MYSQL_USERNAME} \
       --mysql.password=${MYSQL_PASSWORD} \
       --query.time=${QUERY_TIME} \
       --bootstrap.servers=${BOOTSTRAP_SERVERS} 2>&1 &
   fi

}

#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud dyncar服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################

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
