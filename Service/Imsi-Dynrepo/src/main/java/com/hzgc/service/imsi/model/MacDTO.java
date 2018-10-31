package com.hzgc.service.imsi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MacDTO implements Serializable {

    private Integer id;

    private String sn;

    private String mac;

    private String wifisn;

    private Date time;
}
