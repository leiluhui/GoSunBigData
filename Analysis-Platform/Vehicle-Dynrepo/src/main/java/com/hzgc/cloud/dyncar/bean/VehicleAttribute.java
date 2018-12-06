package com.hzgc.cloud.dyncar.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VehicleAttribute implements Serializable{
    private String attributeName;
    private List<String> attributeCodes;

}
