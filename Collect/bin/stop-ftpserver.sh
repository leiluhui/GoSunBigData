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
COLLECT_JAR_NAME=`ls ${LIB_DIR} | grep ^collect-ftp.jar$`          ##获取collect的jar包名称
COLLECT_JAR=${LIB_DIR}/${COLLECT_JAR_NAME}                        ##获取jar包的全路径
COLLECT_JAR_PID=`jps | grep ${COLLECT_JAR_NAME} | awk '{print $1}'`             ##获取PID
CRONTAB_FILE=/var/spool/cron/${USERNAME}

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
	if [ -n "${COLLECT_JAR_PID}" ];then
        echo "Collect-ftp service is exist, exit with 0, kill service now!!"
            ##杀掉进程
        kill -9 ${COLLECT_JAR_PID}
        echo "stop service successfully!!"
        if [ -n "$1"  ];then
            if [ "$1" = "-force" ];then
                ##清除crontab
                if [ -e "${CRONTAB_FILE}" ];then
                    rm -rf ${CRONTAB_FILE}
                    echo "delete crontab.conf successfully"
                else
                    echo "${CRONTAB_FILE} is not exist"
                fi
            fi
        fi
    else
        echo "Collect service is not start"
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
    stop_springCloud $1
}

main $1
