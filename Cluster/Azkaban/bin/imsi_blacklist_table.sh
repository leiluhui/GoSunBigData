#!/bin/bash
IP=172.18.18.119
PORT=4000
 mysql -u root -h ${IP} -P ${PORT} << EOF
use people;

INSERT INTO t_imsi_blacklist (imsi,currentime )
SELECT f1.imsi, f2.currenttime FROM
(SELECT imsi, datetime FROM
t_imsi_all
WHERE DATE_FORMAT(datetime,"%Y%m%d")=DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),"%Y%m%d")
AND COUNT(imsi) >=10)AS f1 ,t_imsi_filter AS f2
WHERE f1.imsi = f2.imsi
AND DATE_FORMAT(f1.datetime,"%Y%m%d")=f2.currenttime;
EOF
