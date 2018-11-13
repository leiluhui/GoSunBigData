package com.hzgc.common.service.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetectorQueryDTO implements Serializable {
    private Integer id;
    private Long communityId;
    private String detectorCode;
    private String sn;
    private String detectorName;
    private String longitude;
    private String latitude;
    private String region;
    private String community;
    private Long districtId;
}
