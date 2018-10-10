#!/bin/bash
########################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    create-schedule-job-to-zip.sh
## Description: 将定时任务生成job文件并打包成zip包
## Author:      chenke
## Created:     2018-03-27
#########################################################################
#set -x ##用于调试使用，不用的时候可以注释掉

#----------------------------------------------------------------------#
#                              定义变量                                #
#----------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`   ###bin目录
cd ..
AZKABAN_DIR=`pwd`  ###azkaban目录
LOG_DIR=${AZKABAN_DIR}/logs  ###集群log日志目录
LOG_FILE=${LOG_DIR}/create-schedule-job-to-zip.log  ##log日志文件

cd ../../../GoSunBigDataDeploy/project
PROJECT_DIR=`pwd`
PORJECT_CONF_DIR=${PROJECT_DIR}/conf
PROJECT_CONF_FILE=${PORJECT_CONF_DIR}/project-deploy.properties

SCHEMA_FILE="schema-merge-parquet-file.sh"
OFFLINE_FILE="start-face-offline-alarm-job.sh"
DYNAMICSHOW_TABLE="get-dynamicshow-table-run.sh"
MASTER_PROTECT_FILE="master-protect.sh"
TASKTRACKER_PROTECT_FILE="tasktracker-protect.sh"

DEVICE_RECOGIZE_TABLE="device_recogize_table.sh"
IMSI_BLACKLIST_TABLE="imsi_blacklist_table.sh"
NEWPEOPLE_TABLE="newpeople_table.sh"
FUSION_IMSI_TABLE="fusion_imsi_table.sh"
HOUR_COUNT_TABLE="24hour_count_table.sh"
OUTPEOPLE_TABLE="outpeople_table.sh"

mkdir -p ${AZKABAN_DIR}/job
AZKABAN_JOB_DIR=${AZKABAN_DIR}/job

mkdir -p ${AZKABAN_DIR}/zip
AZKABAN_ZIP_DIR=${AZKABAN_DIR}/zip

cd ../..
OBJECT_DIR=`pwd`                                 ## 根目录
CLUSTER_BIN_DIR=/opt/GoSunBigData/Cluster/spark/bin
FACECOMPARE_BIN_DIR=/opt/GoSunBigData/Cluster/FaceCompare/bin
PEOMAN_WORKER_BIN_DIR=/opt/GoSunBigData/Cluster/PeopleManager-background/PeopleManager-background-worker/bin

MYSQL=`grep "mysql_host" ${PROJECT_CONF_FILE} | cut -d "=" -f2`
MYSQLIP=${MYSQL%%:*}
MYSQLPORT=${MYSQL##*:}

cd ${AZKABAN_DIR}/bin

sed -i "s#^IP=.*#IP=${MYSQLIP}#g" `grep -r "IP=" ./*.sh | awk -F ":" '{print $1}'`
sed -i "s#^PORT=.*#PORT=${MYSQLPORT}#g" `grep -r "PORT=" ./*.sh | awk -F ":" '{print $1}'`

if [[ ! -f "${DEVICE_RECOGIZE_TABLE}"  ]]; then
    echo "the device_recogize_table_one_day.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/device_recogize_table_one_day.job
    echo "type=command" > ${AZKABAN_JOB_DIR}/device_recogize_table_one_day.job
 echo "command=sh ${BIN_DIR}/${DEVICE_RECOGIZE_TABLE}" >> ${AZKABAN_JOB_DIR}/device_recogize_table_one_day.job
fi

if [[ ! -f "${NEWPEOPLE_TABLE}"  ]]; then
    echo "the newpeople_table_one_month.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/newpeople_table_one_month.job
    echo "type=command" > ${AZKABAN_JOB_DIR}/newpeople_table_one_month.job
    echo "command=sh ${BIN_DIR}/${NEWPEOPLE_TABLE}" >> ${AZKABAN_JOB_DIR}/newpeople_table_one_month.job
fi

if [[ ! -f "${OUTPEOPLE_TABLE}"  ]]; then
    echo "the outpeople_table_one_month.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/outpeople_table_one_month.job
    echo "type=command" > ${AZKABAN_JOB_DIR}/outpeople_table_one_day.job
    echo "command=sh ${BIN_DIR}/${OUTPEOPLE_TABLE}" >> ${AZKABAN_JOB_DIR}/outpeople_table_one_day.job
fi

if [[ ! -f "${IMSI_BLACKLIST_TABLE}"  ]]; then
    echo "the imsi_blacklist_table_one_day.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/imsi_blacklist_table_one_day.job
    echo "type=command" >> ${AZKABAN_JOB_DIR}/imsi_blacklist_table_one_day.job
    echo "command=sh ${BIN_DIR}/${IMSI_BLACKLIST_TABLE}" >> ${AZKABAN_JOB_DIR}/imsi_blacklist_table_one_day.job
fi

if [[ ! -f "${FUSION_IMSI_TABLE}"  ]]; then
    echo "the fusion_imsi_table_one_day.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/fusion_imsi_table_one_day.job
    echo "type=command" > ${AZKABAN_JOB_DIR}/fusion_imsi_table_one_day.job
    echo "command=sh ${BIN_DIR}/${FUSION_IMSI_TABLE}" >> ${AZKABAN_JOB_DIR}/fusion_imsi_table_one_day.job
    echo "dependencies=imsi_blacklist_table_one_day" >> ${AZKABAN_JOB_DIR}/fusion_imsi_table_one_day.job
fi

if [[ ! -f "${HOUR_COUNT_TABLE}"  ]]; then
    echo "the 24hour_count_table_one_hour.sh is not exist!!!"
    else
    touch ${AZKABAN_JOB_DIR}/24hour_count_table_one_hour.job
    echo "type=command" > ${AZKABAN_JOB_DIR}/24hour_count_table_one_hour.job
    echo "command=sh ${BIN_DIR}/${HOUR_COUNT_TABLE}" >> ${AZKABAN_JOB_DIR}/24hour_count_table_one_hour.job
fi

cd ${AZKABAN_ZIP_DIR}
zip device_recogize_table_one_day.zip ${AZKABAN_JOB_DIR}/device_recogize_table_one_day.job
zip fusion_imsi_table_one_day.zip ${AZKABAN_JOB_DIR}/fusion_imsi_table_one_day.job ${AZKABAN_JOB_DIR}/imsi_blacklist_table_one_day.job
zip 24hour_count_table_one_hour.zip ${AZKABAN_JOB_DIR}/24hour_count_table_one_hour.job
zip newpeople_table_one_month.zip ${AZKABAN_JOB_DIR}/newpeople_table_one_month.job
zip outpeople_table_one_day.zip ${AZKABAN_JOB_DIR}/outpeople_table_one_day.job

cd ${CLUSTER_BIN_DIR}  ##进入cluster的bin目录
mkdir -p schema-parquet-one-hour
if [ ! -f "${SCHEMA_FILE}" ]; then
   echo "The schema-merge-parquet-file.sh is not exist!!!"
else
   touch mid_table-one-hour.job     ##创建mid_table-one-hour.job文件
   echo "type=command" > mid_table-one-hour.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> mid_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh mid_table" >> mid_table-one-hour.job

   touch person_table-one-hour.job  ##创建person_table-one-hour.job文件
   echo "type=command" > person_table-one-hour.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> person_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table now" >> person_table-one-hour.job
   echo "dependencies=mid_table-one-hour" >> person_table-one-hour.job

   touch person_table_one-day.job  ##创建person_table_one-day.job文件
   echo "type=command" > person_table_one-day.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> person_table_one-day.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table before" >> person_table_one-day.job

fi
if [ ! -f "${OFFLINE_FILE}" ]; then
   echo "The start-face-offline-alarm-job.sh is not exist!!!"
else
   touch start-face-offline-alarm-job.job  ##创建离线告警的job文件
   echo "type=command" > start-face-offline-alarm-job.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> start-face-offline-alarm-job.job
   echo "command=sh \${cluster_home}/start-face-offline-alarm-job.sh" >> start-face-offline-alarm-job.job
fi


cd ${CLUSTER_BIN_DIR}
mv mid_table-one-hour.job person_table-one-hour.job  schema-parquet-one-hour
zip schema-parquet-one-hour.zip schema-parquet-one-hour/*
zip person_table_one-day.job.zip person_table_one-day.job
zip start-face-offline-alarm-job_oneday.job.zip start-face-offline-alarm-job.job
rm -rf person_table_one-day.job start-face-offline-alarm-job.job schema-parquet-one-hour


cd ${FACECOMPARE_BIN_DIR}
if [ ! -f "$MASTER_PROTECT_FILE" ]; then
    echo "The master-protect.sh is not exist!!!"
else
    touch master-protect-five-second.job
    echo "type=command" > master-protect-five-second.job
    echo "cluster_home=${FACECOMPARE_BIN_DIR}" >> master-protect-five-second.job
    echo "command=sh \${FACECOMPARE_BIN_DIR}/master-protect.sh" >> master-protect-five-second.job
fi
if [ ! -f "$TASKTRACKER_PROTECT_FILE" ]; then
    echo "The tasktracker-protect.sh is not exist!!!"
else
    touch task-tracker-five-second.job
    echo "type=command" >  task-tracker-five-second.job
    echo "cluster_home=${FACECOMPARE_BIN_DIR}" >> task-tracker-five-second.job
    echo "command=sh \${FACECOMPARE_BIN_DIR}/tasktracker-protect.sh" >> task-tracker-five-second.job
fi

zip master-protect-five-second.zip master-protect-five-second.job
zip task-tracker-five-second.zip task-tracker-five-second.job
rm -rf master-protect-five-second.job task-tracker-five-second.job

cd ${AZKABAN_DIR}
mv ${CLUSTER_BIN_DIR}/schema-parquet-one-hour.zip ${CLUSTER_BIN_DIR}/person_table_one-day.job.zip ${CLUSTER_BIN_DIR}/start-face-offline-alarm-job_oneday.job.zip  ${FACECOMPARE_BIN_DIR}/master-protect-five-second.zip ${FACECOMPARE_BIN_DIR}/task-tracker-five-second.zip  zip