package com.hzgc.service.platform.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class Region implements Serializable {
    private Long countryId;
    private String  countryName;
    private Long provinceId;
    private String  provinceName;
    private Long cityId;
    private String cityName;
    private Long districtId;
    private String districtName;
    private String parentMergeName;
    private Long id;
    private String code;
    private String name;
    private Long pareId;
    private String path;
    private String pathName;
    private String level;
    private String shortName;
    private String cityCode;
    private String zipCode;
    private String mergerName;
    private String pinYin;
    private String longitude;
    private String latitude;
}
