#!/bin/bash

cd `dirname $0`
SCRIPT_HOME_DIR=`pwd`
ENV_FILE=$SCRIPT_HOME_DIR/.env
DATE_YMD=`date +%Y-%m-%d`
DATE_HMS=`date +%H:%m:%S`
DATE_YMD_HMS="$DATE_YMD $DATE_HMS"

function env_check() {
    if [ -f "$ENV_FILE" ]; then
        CURRENT_IMAGE_VERSION=`cat $ENV_FILE | grep VERSION | awk -F= '{print $2}'`
        if [ -n "$CURRENT_IMAGE_VERSION" ]; then
            print_info "Find iamge version successfull, current image version is [$CURRENT_IMAGE_VERSION]"
        else
            print_error "Find image version failed, please check"
            exit 1
        fi
        CURRENT_IMAGE_REPOSITORY=`cat $ENV_FILE | grep DOCKER_REPOSITOR | awk -F= '{print $2}'`

        if [ -n "$CURRENT_IMAGE_REPOSITORY" ]; then
            print_info "Find iamge repository successfull, current image address is [$CURRENT_IMAGE_REPOSITORY]"
        else
            print_error "Find image repository failed, please check"
            exit 1
        fi

        IMAGE_LIST=`docker images | grep ${CURRENT_IMAGE_REPOSITORY} | awk '{print $1,$2}' OFS=":" | grep -e ${CURRENT_IMAGE_VERSION}`

        if [ -n "$IMAGE_LIST" ]; then
            print_info "Find iamge list successfull, current image list is:"
            echo "========================================================="
            for image in $IMAGE_LIST
            do
                echo $image
            done
            echo "========================================================="
        else
            print_error "Find image list failed, please check"
            exit 1
        fi

        print_info "Start update image from $CURRENT_IMAGE_REPOSITORY, image version:$CURRENT_IMAGE_VERSION"
        for image in $IMAGE_LIST
        do
            docker rmi $image -f
            docker pull $image
        done
    else
        print_error "Find .env file failed, please check"
        exit 1
    fi
}

function print_error() {
    printf "\033[31m$DATE_YMD_HMS $1 \033[0m\n"
}

function print_info {
    printf "\033[32m$DATE_YMD_HMS $1 \033[0m\n"
}

function main() {
    env_check
}

main