package com.hzgc.cloud.people.service;

import com.hzgc.common.service.sql.Table;

public interface People_table extends Table {

    public String SQL_PEOPLE = "CREATE TABLE IF NOT EXISTS  `t_people` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `name` varchar(32) NOT NULL COMMENT '人员姓名',\n" +
            "  `idcard` varchar(18) NOT NULL COMMENT '身份证',\n" +
            "  `region` bigint(20) NOT NULL COMMENT '区域ID(省市区)',\n" +
            "  `household` varchar(100) DEFAULT NULL COMMENT '户籍',\n" +
            "  `address` varchar(100) DEFAULT NULL COMMENT '现居地',\n" +
            "  `sex` varchar(2) DEFAULT '0' COMMENT '性别',\n" +
            "  `age` int(2) DEFAULT NULL COMMENT '年龄',\n" +
            "  `birthday` varchar(10) DEFAULT NULL COMMENT '出生日期',\n" +
            "  `politic` varchar(10) DEFAULT NULL COMMENT '政治面貌',\n" +
            "  `edulevel` varchar(10) DEFAULT NULL COMMENT '文化程度',\n" +
            "  `job` varchar(32) DEFAULT NULL COMMENT '职业',\n" +
            "  `birthplace` varchar(20) DEFAULT NULL COMMENT '籍贯',\n" +
            "  `community` bigint(20) DEFAULT NULL COMMENT '小区ID',\n" +
            "  `lasttime` timestamp NULL DEFAULT NULL COMMENT '最后出现时间',\n" +
            "  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  UNIQUE KEY `idcard` (`idcard`),\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='人口库';";

    public String SQL_CAR = "CREATE TABLE IF NOT EXISTS  `t_car` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `car` varchar(50) NOT NULL COMMENT '车辆信息',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='车辆信息表';";

    public String SQL_FLAG = "CREATE TABLE IF NOT EXISTS  `t_flag` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `flagid` int(2) NOT NULL COMMENT '标签标识ID',\n" +
            "  `flag` varchar(10) NOT NULL COMMENT '标签',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='标签表';";

    public String SQL_HOUSE = "CREATE TABLE IF NOT EXISTS  `t_house` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `house` varchar(100) NOT NULL COMMENT '房产信息',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='房产信息表';";

    public String SQL_IMSI = "CREATE TABLE IF NOT EXISTS  `t_imsi` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `imsi` varchar(20) NOT NULL COMMENT 'IMSI码',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='IMSI码表';";

    public String SQL_PHONE = "CREATE TABLE IF NOT EXISTS  `t_phone` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `phone` varchar(11) NOT NULL COMMENT '联系方式',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='联系方式表';";

    public String SQL_PICTURE = "CREATE TABLE IF NOT EXISTS  `t_picture` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `idcardpic` longblob DEFAULT NULL COMMENT '证件照片',\n" +
            "  `capturepic` longblob DEFAULT NULL COMMENT '实际采集照片',\n" +
            "  `feature` varchar(8192) NOT NULL COMMENT '特征值',\n" +
            "  `bitfeature` varchar(512) NOT NULL COMMENT 'bit特征值',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='照片信息库';";

    public String SQL_IMEI = "CREATE TABLE IF NOT EXISTS `t_imei` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `imei` varchar(20) NOT NULL COMMENT 'IMEI码',\n" +
            "  `guardianName` varchar(20) NOT NULL COMMENT '监护人名称',\n" +
            "  `guardianPhone` varchar(11) COMMENT '监护人联系方式',\n" +
            "  `cadresName` varchar(20) COMMENT '负责干部名称',\n" +
            "  `cadresPhone` varchar(11) COMMENT '负责干部联系方式',\n" +
            "  `policeName` varchar(20) COMMENT '负责干警名称',\n" +
            "  `policePhone` varchar(11) COMMENT '负责干警联系方式',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `id` (`id`),\n" +
            "  KEY `T_INDEX_PEOPLE_ID` (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=30001 COMMENT='IMEI码表';";

    public String SQL_INNER_FEATURE = "CREATE TABLE IF NOT EXISTS  `t_inner_feature` (\n" +
            "  `peopleid` varchar(32) NOT NULL COMMENT '人员全局ID',\n" +
            "  `feature` varchar(8192) NOT NULL COMMENT '特征值',\n" +
            "  `bitfeature` varchar(512) NOT NULL COMMENT 'bit特征值',\n" +
            "  PRIMARY KEY (`peopleid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='内部人脸特征库';";

    public String GLOBAL = "SET GLOBAL sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";

    public String SESSION = "SET SESSION sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";

}
