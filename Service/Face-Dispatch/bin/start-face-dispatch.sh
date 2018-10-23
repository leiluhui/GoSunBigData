#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start facedispatch
## Description: 启动 facedispatch服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
FACE_DISPATCH_DIR=`pwd`                     ##face-dispatch目录地址
LIB_DIR=${FACE_DISPATCH_DIR}/lib            ##lib目录地址
CONF_DIR=${FACE_DISPATCH_DIR}/conf          ##conf目录地址
FACE_DISPATCH_JAR_NAME=`ls ${LIB_DIR} | grep ^face-dispatch-[0-9].[0-9].[0-9].jar$`          ##获取face-dispatch的jar包名称
FACE_DISPATCH_JAR=${LIB_DIR}/${FACE_DISPATCH_JAR_NAME}                        ##获取jar包的全路径



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000            ##服务注册中心端口
KAFKA_HOST=172.18.18.105:9092
MYSQL_HOST=172.18.18.105:3306
MYSQL_USERNAME=
MYSQL_PASSWORD=

#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_clustering
# 描述: 启动 springCloud device服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   FACE_DISPATCH_PID=`jps | grep ${FACE_DISPATCH_JAR_NAME} | awk '{print $1}'`
   if [  -n "${FACE_DISPATCH_PID}" ];then
      echo "FaceDispatch service already started!!"
   else
      nohup java -jar ${FACE_DISPATCH_JAR} --spring.profiles.active=pro \
      --spring.cloud.config.enabled=false \
      --eureka.ip=${EUREKA_IP} \
      --bootstrap.servers=${KAFKA_HOST} \
      --mysql.host=${MYSQL_HOST} \
      --mysql.username=${MYSQL_USERNAME} \
      --mysql.password=${MYSQL_PASSWORD} \
      --eureka.port=${EUREKA_PORT}  2>&1 &

   fi
}
#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud face-dispatch服务
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