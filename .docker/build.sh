#!/bin/bash

cd `dirname $0`
SCRIPT_HOME_DIR=`pwd`
cd ..
PROJECT_HOME_DIR=`pwd`
DATE_YMD=`date +%Y-%m-%d`
DATE_HMS=`date +%H:%m:%S`
DATE_YMD_HMS="$DATE_YMD $DATE_HMS"
PROJECT_POM=${PROJECT_HOME_DIR}/pom.xml
PROJECT_VERSION=`awk -v RS="</*version>" 'NR==2{print}' ${PROJECT_POM}`
DEFAULT_DOCKER_REPOSTORY_ADDRESS=registry.cn-hangzhou.aliyuncs.com
DOCKER_REPOSITORY_GOURP=hzgc
MAKE_RESULT=$SCRIPT_HOME_DIR/make_result


function find_make()
{
    for make in `find $1 | grep target/make.sh`
    do

        sh $make $PROJECT_VERSION $DOCKER_REPOSITORY_ADDRESS/$DOCKER_REPOSITORY_GOURP
        IMAGE_NAME=`cat $make | grep IMAGE_NAME=| awk -F= '{print $2}'`
        echo $DOCKER_REPOSITORY_ADDRESS/$DOCKER_REPOSITORY_GOURP/$IMAGE_NAME:$PROJECT_VERSION >> ${MAKE_RESULT}
    done
}

function find_push() {
    for name in `cat $MAKE_RESULT`
    do
        if [ -n "$name" ]; then
            docker push $name
        fi
    done
}

function env_check()
{
    command -v docker > /dev/null
    if [ $? = "1" ]; then
        print_error "Docker is not installed, please check"
    fi

    if [ -f "$MAKE_RESULT" ]; then
        print_info "Delete cache file:$MAKE_RESULT"
        rm -f $MAKE_RESULT
    fi

    if [ -z "$PROJECT_VERSION" ]; then
        print_error "Current project version is not found, please check $PROJECT_POM"
        exit 1
    fi

    if [ -z "${DOCKER_REPOSITORY_ADDRESS}" ]; then
        print_info "Current docker repository is not specified, use default:$DEFAULT_DOCKER_REPOSTORY_ADDRESS"
        export DOCKER_REPOSITORY_ADDRESS=${DEFAULT_DOCKER_REPOSTORY_ADDRESS}
    else
        export DOCKER_REPOSITORY_ADDRESS=${1}
    fi
}

function print_error() {
    printf "\033[31m$DATE_YMD_HMS $1 \033[0m\n"
}

function print_info {
    printf "\033[32m$DATE_YMD_HMS $1 \033[0m\n"
}

function modify_version(){
    for version in `find $1 | grep /.env`
    do
       sed -i "s#VERSION=.*#VERSION=${PROJECT_VERSION}#g" $version
       print_info "Modify version successfully, file:$version, version:${PROJECT_VERSION}"
    done
}


function main()
{
    env_check
    find_make $PROJECT_HOME_DIR
    find_push
    modify_version $PROJECT_HOME_DIR
}
main