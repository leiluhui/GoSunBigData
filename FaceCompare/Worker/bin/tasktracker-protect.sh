#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    tasktracker-protest.sh
## Description: to protest tasktracker
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
flag_tasktracker=0   #标志ftp 进程是否在线
CHECK_LOG_FILE=${LOG_DIR}/check_tasktracker.log

####################################################################
# 函数名:check_tasktracker
# 描述：检查tasktracker是否健康运行
# 参数: N/A
# 返回值: N/A
# 其他: N/A
####################################################################
function check_tasktracker()
{
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    echo ""  | tee -a $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    source /etc/profile;
    tasktracker_pid=$(jps | grep TaskTrackerStart)
    sleep 2s
    if [ -n "${tasktracker_pid}" ];then
        echo "current time : $(date)"  | tee -a $CHECK_LOG_FILE
        echo "tasktracker process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "tasktracker process is not exit, just to restart tasktracker."   | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-tasktracker.sh
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 35s
        tasktracker_pid_restart=$(jps | grep TaskTrackerStart)
        if [ -z "${tasktracker_pid_restart}" ];then
            echo "first trying start tasktracker failed.....,retrying to start it second time"  | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-tasktracker.sh
            sleep 35s
            tasktracker_pid_retry=$(jps | grep TaskTrackerStart)
            if [ -z  "${tasktracker_pid_retry}" ];then
                echo "retry start tasktracker failed, please check the config......exit with 1"  | tee -a  $CHECK_LOG_FILE
                flag_tasktracker=1
            else
                echo "secondary try start tasktracker sucess. exit with 0."  | tee -a  $CHECK_LOG_FILE
            fi
        else
            echo "trying to start tasktracker sucess. exit with 0."  | tee -a  $CHECK_LOG_FILE
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
       check_tasktracker
  #     sleep 5m
  # done
}

## 脚本主要业务入口
main
