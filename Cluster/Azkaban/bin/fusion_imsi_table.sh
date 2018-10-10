#!/bin/bash
IP=172.18.18.202
PORT=4000
 mysql -u root -h ${IP} -P ${PORT} << EOF
use people;
INSERT INTO t_imsi (peopleid,imsi)
SELECT peopleid,imsi
FROM(
    SELECT peopleid,imsi,MAX(counted)
    FROM(
        SELECT DISTINCT fi.peopleid, fi.imsi, fi.receivetime,COUNT(DISTINCT fi.peopleid,fi.imsi,fi.receivetime) AS counted
        FROM t_fusion_imsi AS fi LEFT JOIN t_imsi_blacklist AS ib
        ON DATE_FORMAT(fi.receivetime,'%Y%m%d') = DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y%m%d')
        AND fi.imsi != ib.imsi
        GROUP BY fi.imsi, fi.peopleid
        )AS counts
    GROUP BY peopleid
)AS final;
EOF
