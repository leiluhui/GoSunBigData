#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    expend_conf.properties
## Description: 同步新老配置文件的脚本
## Version:     2.0
## Author:      zhangbaolin && yinhang
## Created:     2018-7-2
################################################################################
#set -e
#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 主集群配置文件目录
CLUSTER_CONF_DIR=${ROOT_HOME}/conf
##扩展集群配置文件目录
EXPAND_CONF_DIR=${ROOT_HOME}/expand/conf
## 安装日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/synConf.log
## 集群扩展的节点
LOCAL_HOST=`hostname -i`

component=$1

function main()
{
##在主配置文件中追加组件节点

        CLUSTER=$(grep Cluster_HostName ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${CLUSTER}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Cluster_HostName中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Cluster_HostName=.*#Cluster_HostName=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Cluster_HostName中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加Gsfacelib服务"
        GSFACELIB=$(grep GsFaceLib_HostName ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${GSFACELIB}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件GsFaceLib_HostName中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#GsFaceLib_HostName=.*#GsFaceLib_HostName=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件GsFaceLib_HostName中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加mysql服务"
        MYSQL=$(grep Mysql_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${MYSQL}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Mysql_InstallNode中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Mysql_InstallNode=.*#Mysql_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Mysql_InstallNode中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加hive服务"
        HIVE=$(grep Meta_ThriftServer ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${HIVE}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件ThriftServer中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Meta_ThriftServer=.*#Meta_ThriftServer=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件ThriftServer中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加regionserver服务"
        REGIONSERVER=$(grep HBase_HRegionServer ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${REGIONSERVER}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件RegionServer中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#HBase_HRegionServer=.*#HBase_HRegionServer=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件RegionServer中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加ES服务"
        ES=$(grep ES_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${ES}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件ES中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#ES_InstallNode=.*#ES_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件ES中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加Kibana服务"
        Kibana=$(grep Kibana_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${Kibana}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Kibana中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Kibana_InstallNode=.*#Kibana_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Kibana中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加kafka服务"
        KAFKA=$(grep Kafka_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${KAFKA}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Kafka中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Kafka_InstallNode=.*#Kafka_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Kafka中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加rocketmq服务"
        ROCKETMQ=$(grep RocketMQ_Broker ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${ROCKETMQ}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件RocketMQ中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#RocketMQ_Broker=.*#RocketMQ_Broker=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件RocketMQ中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加zookeeper服务"
        ZOOKEEPER=$(grep Zookeeper_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${ZOOKEEPER}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Zookeeper中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Zookeeper_InstallNode=.*#Zookeeper_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Zookeeper中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加scala服务"
        SCALA=$(grep Scala_InstallNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${SCALA}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Scala中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Scala_InstallNode=.*#Scala_InstallNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Scala中加入节点:${LOCAL_HOST}"
        fi

        echo "在主配置文件中添加spark服务"
        SPARKService=$(grep Spark_ServiceNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${SPARKService}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Spark中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Spark_ServiceNode=.*#Spark_ServiceNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Spark中加入节点:${LOCAL_HOST}"
        fi

        SPARK=$(grep Spark_NameNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${SPARK}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Spark中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#Spark_NameNode=.*#Spark_NameNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Spark中加入节点:${LOCAL_HOST}"
        fi

        HAPROXY=$(grep HAproxy_AgencyNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${HAPROXY}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Haproxy_agency中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#HAproxy_AgencyNode=.*#HAproxy_AgencyNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Haproxy_agency中加入节点:${LOCAL_HOST}"
        fi

        HAPROXYService=$(grep HAproxy_ServiceNode ${ROOT_HOME}/conf/cluster_conf.properties |cut -d '=' -f2)
        if [[ "${HAPROXYService}" =~ "${LOCAL_HOST}" ]] ; then
            echo "配置文件Haproxy_service中已存在此节点:${LOCAL_HOST}"
	    else
            sed -i "s#HAproxy_ServiceNode=.*#HAproxy_ServiceNode=${LOCAL_HOST}#g" ${ROOT_HOME}/conf/cluster_conf.properties
            echo "在主配置文件Haproxy_service中加入节点:${LOCAL_HOST}"
        fi

}

main

