package com.hzgc.common.faceclustering;

import java.io.Serializable;
import java.util.List;

public class PeopleInAttribute implements Serializable {
    //聚类ID
    private String clusteringId;

    //首页显示图片URL
    private String ftpUrl;

    //抓拍告警首次出现时间
    private String firstAppearTime;

    //首次出现图片IPcId
    private String firstIpcId;

    //抓怕告警最后出现时间
    private String lastAppearTime;

    //最后出现图片IPcId
    private String lastIpcId;

    //抓怕告警次数
    private int count;

    //ignore flag,yes/no
    private String flag;

    //抓怕告警记录HBase的rowKey
    private List<String> rowKeys;

    public String getClusteringId() { return clusteringId; }
    public void setClusteringId(String clusteringId) { this.clusteringId = clusteringId; }
    public String getFtpUrl() { return ftpUrl; }
    public void setFtpUrl(String ftpUrl) { this.ftpUrl = ftpUrl; }
    public String getFirstAppearTime() { return firstAppearTime; }
    public void setFirstAppearTime(String firstAppearTime) { this.firstAppearTime = firstAppearTime; }
    public String getFirstIpcId() { return firstIpcId; }
    public void setFirstIpcId(String firstIpcId) { this.firstIpcId = firstIpcId; }
    public String getLastAppearTime() { return lastAppearTime; }
    public void setLastAppearTime(String lastAppearTime) { this.lastAppearTime = lastAppearTime; }
    public String getLastIpcId() { return lastIpcId; }
    public void setLastIpcId(String lastIpcId) { this.lastIpcId = lastIpcId; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
    public List<String> getRowKeys() { return rowKeys; }
    public void setRowKeys(List<String> rowKeys) { this.rowKeys = rowKeys; }
}
