package com.hzgc.service.dyncar.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VehicleAttribute implements Serializable{
    private String attributeName;
    private String logistic;
    private List<String> attributeCodes;

}
