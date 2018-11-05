#!/usr/bin/env bash

DOCKER_LOG_HOME=/opt/logs
DOCKER_IMAGE_NAME=172.18.18.122/hzgc/collect-ftp:1.0
ARGS=(docker
run
-it
--rm
-d
--name collect-ftp
--net host
--runtime=nvidia
-e ZOOKEEPER_HOST=
-e HOST_IP= 
-e DETECTOR_NUMBER=6 
-e KAFKA_HOST= 
-e SEEMMO_URL=
-e HOME_DIR=/opt/ftpdata 
-e BACKUP_DIR=/home/ftpdata 
-e DETECTOR_ENABLE=true 
-e EUREKA_IP= 
-e EUREKA_PORT= 
-e LD_LIBRARY_PATH=/opt/GsFaceLib/face_libs:$LD_LIBRARY_PATH 
-v $DOCKER_LOG_HOME/collect-ftp/log:/log 
-v /opt/GsFaceLib:/opt/GsFaceLib 
-v /opt/ftpdata:/opt/ftpdata 
-v /home/ftpdata:/home/ftpdata
$DOCKER_IMAGE_NAME
)


if [ ! -d "/opt/GsFaceLib" ]; then
    echo -e "\033[31m /otp/GsFaceLib is not exists! \033[0m"
    exit 0
fi

COMMANDS=""
for cmd in ${ARGS[@]}; do
    COMMANDS="$COMMANDS $cmd"
done

echo -e "\033[32m $COMMANDS \033[0m"

exec $COMMANDS