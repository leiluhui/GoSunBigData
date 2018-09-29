#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start collect
## Description: 启动 collect服务
## Author:      zhaozhe
## Created:     2018-09-25
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
COLLECT_DIR=`pwd`                     ##collect目录地址
LIB_DIR=${COLLECT_DIR}/lib            ##lib目录地址
CONF_DIR=${COLLECT_DIR}/conf          ##conf目录地址
COLLECT_JAR_NAME=`ls ${LIB_DIR} | grep ^collect-ftp-[0-9].[0-9].jar$`          ##获取collect的jar包名称
COLLECT_JAR=${LIB_DIR}/${COLLECT_JAR_NAME}                        ##获取jar包的全路径
CRONTAB_FILE=${LIB_DIR}/crontab.conf

#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.191     ##注册中心的ip地址
EUREKA_PORT=9000            ##服务注册中心端口
FTP_IP=172.18.18.105
DETECTOR_NUMBER=6
ROCKETMQ_HOST=172.18.18.107:9876
KAFKA_HOST=172.18.18.105:9092
SEEMMO_URL=http://172.18.18.138:8000/?cmd=recogPic
HOME_DIR=/opt/ftpdata
BACKUP_DIR=/home/ftpdata
DETECTOR_ENABLE=true
ZOOKEEPER_HOST=172.18.18.105:2181

#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_springCloud
# 描述: 启动 springCloud collect-ftp服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   COLLECT_PID=`jps | grep ${COLLECT_JAR_NAME} | awk '{print $1}'`
   if [  -n "${COLLECT_PID}" ];then
      echo "Collect-ftp service already started!!"
   else
      nohup java -jar ${COLLECT_JAR} --spring.profiles.active=pro \
      --spring.cloud.config.enabled=false \
      --zk.host=${ZOOKEEPER_HOST} \
      --ftp.ip=${FTP_IP} \
      --detector.number=${DETECTOR_NUMBER} \
      --rocketmq.host=${ROCKETMQ_HOST} \
      --kafka.host=${KAFKA_HOST} \
      --seemmo.url=${SEEMMO_URL} \
      --home.dir=${HOME_DIR} \
      --backup.dir=${BACKUP_DIR} \
      --detector.enable=${DETECTOR_ENABLE} \
      --eureka.ip=${EUREKA_IP} \
      --eureka.port=${EUREKA_PORT}  2>&1 &
   fi
}

#####################################################################
# 函数名: drop_caches
# 描述: ftp 启动的时候，在每个ftp 服务器起一个清楚缓存的定时任务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function drop_caches()
{
    echo "* */1 * * * root sync;echo 3 > /proc/sys/vm/drop_caches" > ${CRONTAB_FILE}
}

#####################################################################
# 函数名: ftp_protect
# 描述: ftp 启动的时候，为FTP添加定时检测服务是否挂掉脚本，保证FTP挂掉后可以重新拉起
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function ftp_protect()
{
    echo "*/1 * * * * root sync;sh ${BIN_DIR}/ftp_protect.sh" >> ${CRONTAB_FILE}
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
    `source /etc/profile`
    `systemctl restart crond`
    drop_caches
    ftp_protect
    `crontab ${CRONTAB_FILE}`
    start_springCloud
}

## 脚本主要业务入口
main
