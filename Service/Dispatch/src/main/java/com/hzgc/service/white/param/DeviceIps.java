package com.hzgc.service.white.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceIps implements Serializable {
    private String deviceName;
    private String deviceCode;
}
