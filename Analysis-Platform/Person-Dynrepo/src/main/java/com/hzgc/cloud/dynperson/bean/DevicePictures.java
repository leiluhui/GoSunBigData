package com.hzgc.cloud.dynperson.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DevicePictures implements Serializable {

    private String deviceId;

    private String deviceName;

    private List<Pictures> pictures;

    private int total;

}
