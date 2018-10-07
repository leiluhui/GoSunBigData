#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    master-protect.sh
## Description: to protect master
## Author:      wujiaqi
## Created:     2018-09-17
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                                               ### bin目录
cd ..
COMPARE_DIR=`pwd`                                           ### compare目录
LOG_DIR=${COMPARE_DIR}/log                                  ### log目录
CONF_DIR=${COMPARE_DIR}/conf                                ### conf目录
LIB_DIR=${COMPARE_DIR}/lib                                  ### lib目录
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
flag_master=0   #标志master 进程是否在线
CHECK_LOG_FILE=${LOG_DIR}/check_master.log

####################################################################
# 函数名:check_master
# 描述：检查master是否健康运行
# 参数: N/A
# 返回值: N/A
# 其他: N/A
####################################################################
function check_master()
{
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    echo ""  | tee -a $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    source /etc/profile;
    master_pid=$(jps | grep FaceCompareMaster)
    sleep 2s
    if [ -n "${master_pid}" ];then
        echo "current time : $(date)"  | tee -a $CHECK_LOG_FILE
        echo "master process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "master process is not exit, just to restart master."   | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-master.sh
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 35s
        master_pid_restart=$(jps | grep FaceCompareMaster)
        if [ -z "${master_pid_restart}" ];then
            echo "first trying start master failed.....,retrying to start it second time"  | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-master.sh
            sleep 35s
            master_pid_retry=$(jps | grep FaceCompareMaster)
            if [ -z  "${master_pid_retry}" ];then
                echo "retry start master failed, please check the config......exit with 1"  | tee -a  $CHECK_LOG_FILE
                flag_master=1
            else
                echo "secondary try start master sucess. exit with 0."  | tee -a  $CHECK_LOG_FILE
            fi
        else
            echo "trying to start master sucess. exit with 0."  | tee -a  $CHECK_LOG_FILE
        fi
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
#   while true
 #  do
       check_master
  #     sleep 5m
  # done
}

## 脚本主要业务入口
main
