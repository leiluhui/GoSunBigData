package com.hzgc.cluster.spark.clustering;

import java.util.List;

public class RealName {
    String region;
    String regionName;
    List<String> ipcIds;
    String lastRuntime;
    String interval;
    String sim;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLastRuntime() {
        return lastRuntime;
    }

    public void setLastRuntime(String lastRuntime) {
        this.lastRuntime = lastRuntime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public List<String> getIpcIds() {
        return ipcIds;
    }

    public void setIpcIds(List<String> ipcIds) {
        this.ipcIds = ipcIds;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
