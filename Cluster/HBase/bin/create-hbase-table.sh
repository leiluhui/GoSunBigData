#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     create-dynamic-index.sh
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
LOG_DIR=${HBASE_DIR}/log
LOG_FILE=${LOG_DIR}/create-hbase-table.log
cd ..
CLUSTER_DIR=`pwd`                               ### cluster 目录
SPARK_DIR=${CLUSTER_DIR}/spark
cd ..
OBJECT_DIR=`pwd`                                ### Real 根目录
COMMON_DIR=${OBJECT_DIR}/common                 ### common 目录
CONF_FILE=${COMMON_DIR}/conf/project-conf.properties


## bigdata cluster path
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
## bigdata hive path
SPARK_PATH=${BIGDATA_CLUSTER_PATH}/Spark/spark
## HBase_home
HBASE_HOME=${BIGDATA_CLUSTER_PATH}/HBase/hbase
## bigdata hadoop path
HADOOP_PATH=${BIGDATA_CLUSTER_PATH}/Hadoop/hadoop
## bigdata hive path
HIVE_PATH=${BIGDATA_CLUSTER_PATH}/Hive/hive
## udf function name
UDF_FUNCTION_NAME=compare
## udf class path
UDF_CLASS_PATH=com.hzgc.cluster.spark.udf.UDFArrayCompare
## hdfs udf  path
HDFS_UDF_PATH=/user/hive/udf
## udf jar version
UDF_VERSION=`ls ${SPARK_DIR}/lib | grep ^spark-udf-[0-9].[0-9].[0-9].jar$`
## hdfs udf Absolute path
HDFS_UDF_ABSOLUTE_PATH=hdfs://hzgc/${HDFS_UDF_PATH}/${UDF_VERSION}

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
    echo "**********************************************" | tee -a $LOG_FILE
    echo "please waitinng, 一键创建动态库中'searchRes'表；创建'device'设备表， “clusteringInfo”........"  | tee -a $LOG_FILE
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