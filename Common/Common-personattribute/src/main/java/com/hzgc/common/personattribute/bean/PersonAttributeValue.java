package com.hzgc.common.personattribute.bean;

import java.io.Serializable;

public class PersonAttributeValue implements Serializable {
    private  String desc;
    private  Integer code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
