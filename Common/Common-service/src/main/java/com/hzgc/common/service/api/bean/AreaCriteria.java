package com.hzgc.common.service.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaCriteria implements Serializable {
    private Long id;
    private String level;
    private String fuzzyField;
    private String fuzzyValue;
    private Integer start;
    private Integer limit;
    private String sort;
    private String authIds;
    private String userName;
}