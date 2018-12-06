#!/bin/bash
cd `dirname $0`
HOME_DIR=`pwd`
VERSION_INFO=${1}
IMAGE_NAME=collect-ftp
DOCKER_REPO=${2}
DATE_YMD=`date +%Y-%m-%d`
DATE_HMS=`date +%H:%m:%S`
DATE_YMD_HMS="$DATE_YMD $DATE_HMS"
IMAGE_FINAL_NAME=$IMAGE_NAME:$VERSION_INFO

function make()
{
    if [ -z "$DOCKER_REPO" ]; then
        printf "\033[31m$DATE_YMD_HMS Docker repository is not specified  \033[0m\n"
        exit 1
    fi

    if [ -z "VERSION_INFO" ]; then
        printf "\033[31m$DATE_YMD_HMS Current image name is:$IMAGE_NAME, version:$VERSION_INFO \033[0m\n"
        exit 1
    fi

    exec docker build -t $DOCKER_REPO/$IMAGE_NAME:$VERSION_INFO -f Dockerfile ./
    echo $IMAGE_FINAL_NAME
}

function main()
{
    make
}
main