#!/bin/bash
IP=172.18.18.119
PORT=4000
 mysql -u root -h ${IP} -P ${PORT} << EOF
use people;

INSERT INTO peopleid, community, `month`, 1
SELECT id,community ,time
FROM(
    SELECT community ,time, id
    FROM (
        SELECT id, community, DATE_FORMAT(lasttime,"%Y%m") AS time
        FROM t_people
        WHERE lasttime <= DATE_SUB(now(),INTERVAL 3 MONTH)
          AND community is NOT NULL
    ) AS people, t_picture
    WHERE people.id = t_picture.peopleid
)AS t1 ,t_people_out
WHERE t1.id !=t_people_out.peopleid
AND time = t_people_out.`month`;
EOF
