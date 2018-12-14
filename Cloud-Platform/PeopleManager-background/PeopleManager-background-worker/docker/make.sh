#!/bin/bash
#set -x


cd `dirname $0`
HOME_DIR=`pwd`
#镜像版本,由负责人自己填写
VERSION_INFO=2.4.0
#服务版本信息
VERSION_NAME=PEOMAN_WORKER_VERSION
IMAGE_NAME=peoman-worker
#镜像仓库地址,使用外部传参指定
ACTION=${1}
DOCKER_REPO=${2}
DOCKER_DEFULT=registry.cn-hangzhou.aliyuncs.com/hzgc
DATE_YMD=`date +%Y-%m-%d`
DATE_HMS=`date +%H:%m:%S`
DATE_YMD_HMS="$DATE_YMD $DATE_HMS"
IMAGE_FINAL_NAME=$IMAGE_NAME:$VERSION_INFO

function make()
{
   if [ "build" = "$ACTION"  ];then
     if [ ! -n "${DOCKER_REPO}" ]; then
         exec docker build -t $DOCKER_DEFULT/$IMAGE_NAME:$VERSION_INFO -f Dockerfile ./
     else
         exec docker build -t $DOCKER_REPO/$IMAGE_NAME:$VERSION_INFO -f Dockerfile ./
     fi
   elif [ "push" = "$ACTION"  ];then
       if [ ! -n "${DOCKER_REPO}" ]; then
           exec docker build -t $DOCKER_DEFULT/$IMAGE_NAME:$VERSION_INFO -f Dockerfile ./ &
           sleep 1s
           exec docker push $DOCKER_DEFULT/$IMAGE_NAME:$VERSION_INFO
       else
           exec docker build -t $DOCKER_REPO/$IMAGE_NAME:$VERSION_INFO -f Dockerfile ./ &
           sleep 1s
           exec docker push $DOCKER_REPO/$IMAGE_NAME:$VERSION_INFO
       fi
   else
     echo "usage: "
     echo "1, sh make.sh build                    打成阿里云镜像仓库镜像."
     echo "2, sh make.sh build 镜像仓库地址/hzgc  打成其他镜像仓库镜像. "
     echo "3, sh make.sh push                     打成阿里云镜像仓库镜像，并将镜像推入阿里云镜像仓库."
     echo "4, sh make.sh push 镜像仓库地址/hzgc   打成其他镜像仓库镜像，并将镜像推入对应镜像仓库."
     exit 1;

   fi
}


function main()
{
    make
}

main