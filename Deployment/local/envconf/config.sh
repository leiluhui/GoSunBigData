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
ANALYSIS_BASIS_DIR=${ANALYSIS_DIR}/basis
ANALYSIS_FACECOMPARE_DIR=${ANALYSIS_DIR}/facecompare
ANALYSIS_FTPSERVER_DIR=${ANALYSIS_DIR}/ftpserver
CLOUD_DIR=${DOCKER_COMPOSE_DIR}/cloud
CLOUD_BASIS_DIR=${CLOUD_DIR}/basis
CLOUD_PEOMAN_DIR=${CLOUD_DIR}/peoman
MODULE_DIR=${DOCKER_COMPOSE_DIR}/module

cp -r ${ENV_FILE} ${ANALYSIS_BASIS_DIR}
cp -r ${ENV_FILE} ${ANALYSIS_FACECOMPARE_DIR}
cp -r ${ENV_FILE} ${ANALYSIS_FTPSERVER_DIR}
cp -r ${ENV_FILE} ${CLOUD_BASIS_DIR}
cp -r ${ENV_FILE} ${CLOUD_PEOMAN_DIR}
cp -r ${ENV_FILE} ${MODULE_DIR}