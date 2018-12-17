package com.hzgc.system.transform;

import java.util.Date;

public class DeviceInfo {

    //设备号
    private String deviceSn;
    //设备类型
    private String deviceType;
    //最后上报数据的时间
    private Date lastDataTime;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }
}
