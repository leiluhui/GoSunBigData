#!/bin/bash
#########################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config.sh
## Description: 一键配置env服务模块
## Author:      chenke
## Created:     2018-12-11
##########################################################
#set -x  ## 用于调试用，不用的时候可以注释掉
#set -e

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
ENV_FILE=${BIN_DIR}/.env

cd ../docker-compose
DOCKER_COMPOSE_DIR=`pwd`
ANALYSIS_DIR=${DOCKER_COMPOSE_DIR}/analysis
CLOUD_DIR=${DOCKER_COMPOSE_DIR}/cloud
FACECOMPARE_DIR=${DOCKER_COMPOSE_DIR}/facecompare
FTPSERVER_DIR=${DOCKER_COMPOSE_DIR}/ftpserver
MODULE_DIR=${DOCKER_COMPOSE_DIR}/module
PEOMAN_DIR=${DOCKER_COMPOSE_DIR}/peoman

cp -r ${ENV_FILE} ${ANALYSIS_DIR}
cp -r ${ENV_FILE} ${CLOUD_DIR}
cp -r ${ENV_FILE} ${FACECOMPARE_DIR}
cp -r ${ENV_FILE} ${FTPSERVER_DIR}
cp -r ${ENV_FILE} ${MODULE_DIR}
cp -r ${ENV_FILE} ${PEOMAN_DIR}