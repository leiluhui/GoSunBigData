#!/bin/bash
IP=172.18.18.119
PORT=4000
 mysql -u root -h ${IP} -P ${PORT} << EOF
use people;

#抓拍设备
INSERT INTO t_device_recognize (peopleid,deviceid,date,count,flag,community)
SELECT peopleid,deviceid,DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY,'%Y%m%d') AS time,COUNT(peopleid),1,community
FROM t_people_recognize
WHERE DATE_FORMAT(capturetime,'%Y%m%d')=time
GROUP BY peopleid,deviceid;
#侦码设备
INSERT INTO t_device_recognize (peopleid,deviceid,date,count,flag,community)
SELECT peopleid,deviceid,DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY,'%Y%m%d') AS time,COUNT(peopleid),2,community
FROM t_fusion_imsi
WHERE DATE_FORMAT(receivetime,'%Y%m%d')= time
GROUP BY peopleid,deviceid;
EOF
