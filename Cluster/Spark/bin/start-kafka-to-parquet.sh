#!/bin/bash

#set -x  ## 用于调试使用，不用的时候可以注释掉

JARS=(transport-5.5.0.jar
transport-netty4-client-5.5.0.jar
hppc-0.7.1.jar
log4j-api-2.7.jar
t-digest-3.0.jar
netty-common-4.1.11.Final.jar
rest-5.5.0.jar
parent-join-client-5.5.0.jar
reindex-client-5.5.0.jar
percolator-client-5.5.0.jar
transport-netty3-client-5.5.0.jar
lang-mustache-client-5.5.0.jar
jackson-core-2.8.10.jar
common-util-1.0.jar
spark-streaming-kafka-0-8_2.11-2.2.0.jar
zkclient-0.3.jar
common-jni-1.0.jar
elasticsearch-spark-20_2.11-5.5.0.jar
kafka_2.11-0.8.2.1.jar
elasticsearch-5.5.0.jar
common-seemmo-1.0.jar
kafka-clients-1.0.0.jar
common-collect-1.0.jar
metrics-core-2.2.0.jar
)
## spark class
SPARK_CLASS_PARAM=com.hzgc.cluster.spark.consumer.KafkaToParquet





cd `dirname $0`
cd ..
SPARK_DIR=`pwd`                                        #spark模块目录
cd ..
######## cluster目录 ########
SPARK_CONF_DIR=${SPARK_DIR}/conf
SPARK_LIB_DIR=${SPARK_DIR}/lib
SPARK_LOG_DIR=${SPARK_DIR}/logs
LOG_FILE=${SPARK_LOG_DIR}/KafkaToParquet.log
############ 创建log目录 ###############
if [ ! -d ${SPARK_LOG_DIR} ];then
   mkdir ${SPARK_LOG_DIR}
fi
############ 判断是否存在大数据集群###################
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
if [ ! -d ${BIGDATA_CLUSTER_PATH} ];then
   printf "\033[31m ERROR: ${BIGDATA_CLUSTER_PATH} does not exit,please go to the node of the existing bigdata cluster! \033[0m\n"
   exit 0
fi
############### 判断是否存在配置文件 ##################
if [ ! -f ${SPARK_CONF_DIR}/sparkJob.properties ];then
    printf "\033[31m ERROR: ${SPARK_CONF_DIR}/sparkJob.properties does not exit! \033[0m\n"
    exit 0
else
    cp ${SPARK_CONF_DIR}/sparkJob.properties /opt/hzgc/bigdata/Spark/spark/conf/
fi
if [ ! -f ${SPARK_CONF_DIR}/log4j.properties ];then
    printf "\033[31m ERROR: ${SPARK_CONF_DIR}/log4j.properties does not exit! \033[0m\n"
    exit 0
else
    sed -i "s#^log4j.appender.FILE.File=.*#log4j.appender.FILE.File=${LOG_FILE}#g" ${SPARK_CONF_DIR}/log4j.properties
fi

JARS_PATH=""
for jar in ${JARS[@]}; do
	jar_path=${SPARK_LIB_DIR}/${jar}
	if [ ! -f ${jar_path} ];then
		printf "\033[31m ERROR: ${jar_path} not exist exit \033[0m\n"
		exit 0
	fi
	JARS_PATH=${JARS_PATH}${jar_path},
done


SPARK_API_VERSION=`ls ${SPARK_LIB_DIR} | grep ^spark-[0-9].[0-9].[0-9].jar$`
source /etc/profile
source /opt/hzgc/env_bigdata.sh
nohup spark-submit \
--master yarn-cluster \
--executor-memory 4g \
--executor-cores 2 \
--num-executors 4 \
--class ${SPARK_CLASS_PARAM} \
--jars ${JARS_PATH%?} \
--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=file:${SPARK_CONF_DIR}/log4j.properties" \
--files ${SPARK_CONF_DIR}/sparkJob.properties,\
/opt/hzgc/bigdata/HBase/hbase/conf/hbase-site.xml \
${SPARK_LIB_DIR}/${SPARK_API_VERSION} > ${LOG_FILE} 2>&1 &

if [ $? -eq 0 ];then
    printf "\033[32m SUCCESS: Start kafka to parquet success!!! \033[0m\n"
else
     printf "\033[31m ERROR: Start kafka to parquet Failure!!! \033[0m\n"
     exit 1
fi
