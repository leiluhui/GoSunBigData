package com.hzgc.cloud.fusion.service;

public interface Fusion_table {

    public String SQL_FUSION = "CREATE TABLE IF NOT EXISTS  `t_fusion_imsi` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `community` bigint(20) NOT NULL COMMENT '小区ID',\n" +
            "  `deviceid` varchar(50) NOT NULL COMMENT '帧码设备ID',\n" +
            "  `receivetime` timestamp NOT NULL COMMENT '接收时间',\n" +
            "  `imsi` varchar(20) NOT NULL COMMENT 'imsi码',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='数据融合记录表';";
}
