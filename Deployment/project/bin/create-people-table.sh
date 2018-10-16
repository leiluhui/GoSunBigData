#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    project_distribute.sh
## Description: 一键配置及分发微服务模块
## Author:      zhangbaolin
## Created:     2018-07-26
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉
#set -e

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../..
CONF_DIR=${BIN_DIR}/../conf
SQL_DIR=${BIN_DIR}/../sql
CONF_FILE=${CONF_DIR}/project-deploy.properties
MYSQL_HOST=$(grep mysql_host ${CONF_FILE} | cut -d '=' -f2)
MYSQL_IP=`echo ${MYSQL_HOST} | cut -d ':' -f1`
MYSQL_PORT=`echo ${MYSQL_HOST} | cut -d ':' -f2`
MYSQL_USER=$(grep mysql_user ${CONF_FILE}  | cut -d '=' -f2)
MYSQL_PASSWORD=$(grep mysql_password ${CONF_FILE} | cut -d '=' -f2)

mysql -h ${MYSQL_IP} -u ${MYSQL_USER} -P ${MYSQL_PORT} <<EOF
source ${SQL_DIR}/peopleDatabase.sql;
EOF


#--------------------------------------------------------------------------#
#                                  执行流程                                #
#--------------------------------------------------------------------------#
##打印时间
echo ""
echo "=========================================================="
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

main

set +x


