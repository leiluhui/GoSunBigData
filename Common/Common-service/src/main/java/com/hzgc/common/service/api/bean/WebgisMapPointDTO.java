package com.hzgc.common.service.api.bean;

import lombok.Data;

import java.util.Date;

@Data
public class WebgisMapPointDTO {
    private Integer id;
    private Long deviceId;
    private String deviceCode;
    private Long platId;
    private Double marsLongitude;
    private Double marsLatitude;
    private Double baiduLongitude;
    private Double baiduLatitude;
    private String address;
    private Integer status;
    private String departmentId;
    private String name;
    private Integer type;
    private String mountingHeight;
    private String mountingAngle;
    private Integer pole;
    private String mountingDescribe;
    private String pointCaseNumber;
    private String beforeCaseNumber;
    private String afterCaseNumber;
    private String powerPointDescribe;
    private Integer viewAngle;
    private Integer viewDirection;
    private Integer viewRadius;
    private Date addTime;
    private Date updateTime;
}
