package com.hzgc.common.service.api.bean;

import lombok.Data;

@Data
public class CameraQueryDTO {
    private Integer cameraId;
    private Long communityId;
    private String cameraCode;
    private String cameraName;
    private String longitude;
    private String latitude;
    private String region;
    private String community;
    private Long districtId;
}
