package com.hzgc.service.imsi.model;

import lombok.Data;


@Data
public class MacInfo {
    private Integer id;

    //电围设备
    private String sn;

    //mac地址
    private String mac;

    private String wifisn;

    private String time;

    //小区ID
    private Long communityId;

}