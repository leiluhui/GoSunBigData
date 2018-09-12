#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    admin-master.sh
## Description: 向master发送命令
## Author:      wujiaqi
## Created:     2018-09-11
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                               ### bin目录
cd ..
COMPARE_DIR=`pwd`                                           ### compare目录
CONF_DIR=${COMPARE_DIR}/conf                                ### conf目录
LIB_DIR=${COMPARE_DIR}/lib                                  ### lib目录
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

ARG_1=${1}
ARG_2=${2}

BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata

#####################################################################
# 函数名: admin_master
# 描述: 启动master
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function admin_master()
{
    java -server -Xms1g -Xmx3g -classpath $CONF_DIR:$LIB_JARS com.hzgc.compare.command.RpcClientForMaster ${ARG_1} ${ARG_2}
}

admin_master
