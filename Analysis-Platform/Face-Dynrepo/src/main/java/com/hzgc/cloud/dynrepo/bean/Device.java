package com.hzgc.cloud.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Device implements Serializable {

    private String ipc;
    private String deviceName;
}
