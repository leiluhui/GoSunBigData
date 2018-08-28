package com.hzgc.service.clustering.bean.export;

import lombok.Data;

import java.io.Serializable;

@Data
public class Locus implements Serializable{
    private Long deviceId;
    private Double marsLongitude;
    private Double marsLatitude;
    private int count;
    private String Time;
}
