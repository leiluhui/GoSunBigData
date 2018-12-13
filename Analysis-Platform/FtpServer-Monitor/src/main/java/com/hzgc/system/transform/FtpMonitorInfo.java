package com.hzgc.system.transform;

import java.util.ArrayList;
import java.util.List;

public class FtpMonitorInfo {
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private String clientHostname;
    private String clientIp;
    private String container;
    private Long semmonCount;
    private Long gsfaceCount;

    public List<DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public String getClientHostname() {
        return clientHostname;
    }

    public void setClientHostname(String clientHostname) {
        this.clientHostname = clientHostname;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public Long getSemmonCount() {
        return semmonCount;
    }

    public void setSemmonCount(Long semmonCount) {
        this.semmonCount = semmonCount;
    }

    public Long getGsfaceCount() {
        return gsfaceCount;
    }

    public void setGsfaceCount(Long gsfaceCount) {
        this.gsfaceCount = gsfaceCount;
    }
}
