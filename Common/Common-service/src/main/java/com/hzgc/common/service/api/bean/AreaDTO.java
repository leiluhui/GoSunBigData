package com.hzgc.common.service.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaDTO implements Serializable {
    private Long countryId;
    private String countryName;
    private Long provinceId;
    private String provinceName;
    private Long cityId;
    private String cityName;
    private Long districtId;
    private String districtName;
    private Long townId;
    private String townName;
    private Long villageId;
    private String villageName;
    private String parentMergeName;
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private String path;
    private String pathName;
    private String level;
    private Boolean custom;
    private String shortName;
    private String cityCode;
    private String zipCode;
    private String mergerName;
    private String pinYin;
    private String longitude;
    private String latitude;
    private String coverage;
    private Boolean coverageTransform;
}
