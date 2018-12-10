package com.hzgc.cluster.dispatch.service;

public interface Dispatch_background_table{

    public String SQL_DISPATCH_WHITE = "CREATE TABLE IF NOT EXISTS  t_dispatch_white(\n" +
            "  id VARCHAR(32) PRIMARY KEY NOT NULL COMMENT '白名单ID',\n" +
            "  name VARCHAR(32) NOT NULL COMMENT '白名单名称',\n" +
            "  devices TEXT NOT NULL COMMENT '设备ID列表 ',\n" +
            "  organization TEXT COMMENT '相机组织',\n" +
            "  status INT(2) NOT NULL DEFAULT 0 COMMENT  '状态值(0:开启,1:停止)'\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '白名单库';";

    public String SQL_DISPATCH_WHITEINFO = "CREATE TABLE IF NOT EXISTS  t_dispatch_whiteinfo(\n" +
            "  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '白名单人员ID',\n" +
            "  white_id VARCHAR(32)  NOT NULL COMMENT '白名单ID',\n" +
            "  name VARCHAR(10) COMMENT '人员姓名',\n" +
            "  picture LONGBLOB NOT NULL COMMENT '人员照片',\n" +
            "  feature VARCHAR(8192) NOT NULL COMMENT '特征值',\n" +
            "  bit_feature VARCHAR(64) NOT NULL COMMENT 'bit位特征值'\n" +
            ")ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '白名单人员表';";

    public String SQL_DISPATCH_ALIVE = "CREATE TABLE IF NOT EXISTS  t_dispatch_alive(\n" +
            "  id VARCHAR(32) PRIMARY KEY NOT NULL COMMENT 'ID',\n" +
            "  name VARCHAR(32) COMMENT '布控名称',\n" +
            "  devices TEXT COMMENT  '设备ID列表 ',\n" +
            "  organization TEXT COMMENT '相机组织',\n" +
            "  start_time VARCHAR(10) NOT NULL COMMENT '开始时间',\n" +
            "  end_time VARCHAR(10) NOT NULL COMMENT '结束时间',\n" +
            "  status INT(2) NOT NULL DEFAULT 0 COMMENT  '状态值(0:开启,1:停止)',\n" +
            "  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '活体检测表';";

}
