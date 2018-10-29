package com.hzgc.common.service.api.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AreaSDTO implements Serializable {
    private List<AreaDTO> areaDTOs;
    private Long totalNum;
}
