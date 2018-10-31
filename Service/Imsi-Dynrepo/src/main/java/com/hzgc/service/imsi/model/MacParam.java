package com.hzgc.service.imsi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MacParam implements Serializable{

    private List<String> list;
    private String startTime;
    private String endTime;
}
