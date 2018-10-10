#!/bin/bash
IP=172.18.18.119
PORT=4000
 mysql -u root -h ${IP} -P ${PORT} << EOF
use people;

INSERT INTO t_imsi_blacklist (imsi,currenttime )
SELECT f1.imsi, f2.currenttime
FROM (SELECT imsi, savetime
      FROM (SELECT imsi, savetime ,COUNT(imsi) AS count
            FROM t_imsi_all
            WHERE DATE_FORMAT(savetime,"%Y%m%d")=DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),"%Y%m%d")) AS t1
      WHERE count >=10)AS f1 ,t_imsi_filter AS f2
WHERE f1.imsi = f2.imsi
AND DATE_FORMAT(f1.savetime,"%Y%m%d")=f2.currenttime;
EOF
