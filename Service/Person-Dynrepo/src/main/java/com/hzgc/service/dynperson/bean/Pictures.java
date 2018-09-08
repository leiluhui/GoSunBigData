package com.hzgc.service.dynperson.bean;

import com.hzgc.jniface.PersonAttribute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Pictures implements Serializable {

    private String burl;

    private String deviceId;

    private String deviceName;

    private String surl;

    private String time;

    private List<PersonAttribute> personAttributes;

}
