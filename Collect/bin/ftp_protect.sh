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
COLLECT_JAR_NAME=`ls ${LIB_DIR} | grep ^collect-ftp-[0-9].[0-9].jar$`          ##获取collect的jar包名称
CONF_DIR=$COLLECT_DIR/conf    ### 项目根目录
LOG_DIR=${COLLECT_DIR}/log                       ## log 日记目录
CHECK_LOG_FILE=${LOG_DIR}/check_ftpserver.log

####################################################################
# 函数名:check_ftpserver
# 描述：检查ftp是否健康运行
# 参数: N/A
# 返回值: N/A
# 其他: N/A
####################################################################
function check_ftpserver()
{
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    echo ""  | tee -a $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    source /etc/profile;
    collect_jar_pid=`jps | grep ${COLLECT_JAR_NAME} | awk '{print $1}'`             ##获取PID
    if [ -n "${collect_jar_pid}" ];then
        echo "current time : $(date)"  | tee -a $CHECK_LOG_FILE
        echo "ftp process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "ftp process is not exit, just to restart ftp."   | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-ftpserver.sh
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
    fi
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE

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
    check_ftpserver
}

## 脚本主要业务入口
main
