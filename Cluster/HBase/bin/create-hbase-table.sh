#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     create-hbase-table.sh
## Description:  创建动态库表的所有索引
## Author:       qiaokaifeng
## Created:      2017-11-28
################################################################################

#set -x
#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                   ### bin 目录
cd ..
HBASE_DIR=`pwd`                                 ### hbase 目录
SQL_DIR=${HBASE_DIR}/sql
cd ..
cd ..
SCRIPT_DIR=`pwd`
CONF_FILE=${SCRIPT_DIR}/../conf/project-conf.properties

## bigdata cluster path
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
## HBase_home
HBASE_HOME=${BIGDATA_CLUSTER_PATH}/HBase/hbase

#####################################################################
# 函数名: create_searchRes_device_clusteringInfo_table
# 描述: 创建person表， mid_table 表格
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_searchRes_device_clusteringInfo_table() {
    #---------------------------------------------------------------------#
    #                 创建searchRes, device，clusteringInfo                         #
    #---------------------------------------------------------------------#
    echo "**********************************************"
    echo "please waitinng, 一键创建动态库中'searchRes'表；创建'device'设备表， “clusteringInfo”........"
    sh ${HBASE_HOME}/bin/hbase shell ${SQL_DIR}/hbase.sql
    if [ $? = 0 ];then
        echo "创建成功..."
    else
        echo "创建失败..."
    fi
}

function main() {
    create_searchRes_device_clusteringInfo_table
}

main