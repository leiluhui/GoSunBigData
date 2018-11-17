package com.hzgc.service.imsi.model;

import lombok.Data;


@Data
public class MacInfo {
    private Integer id;

    private String sn;

    private String mac;

    private String wifisn;

    private String time;

    //小区ID
    private Long communityId;

}