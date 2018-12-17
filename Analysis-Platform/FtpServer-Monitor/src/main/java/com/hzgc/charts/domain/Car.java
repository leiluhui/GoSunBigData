package com.hzgc.charts.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * created by liang on 2018/12/12
 */
@Document(indexName = "dynamiccar", type = "car")
public class Car implements Serializable {

    private String id;                  //对象唯一ID

    private String ipcid;               // 设备ID

    private String timestamp;           // 时间（格式：2017-01-01 00：00：00）

    private String hostname;            // 图片保存主机:hostname

    private String sabsolutepath;       //小图存储绝对路径(不带ftp根目录)

    private String babsolutepath;       //大图存储绝对路径(不带ftp根目录)


    private String provinceid;                  // =省

    private String cityid;                  // 市

    private String regionid;                  // 区

    private String communityid;                  // 社区

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getCommunityid() {
        return communityid;
    }

    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpcid() {
        return ipcid;
    }

    public void setIpcid(String ipcid) {
        this.ipcid = ipcid;
    }


    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSabsolutepath() {
        return sabsolutepath;
    }

    public void setSabsolutepath(String sabsolutepath) {
        this.sabsolutepath = sabsolutepath;
    }

    public String getBabsolutepath() {
        return babsolutepath;
    }

    public void setBabsolutepath(String babsolutepath) {
        this.babsolutepath = babsolutepath;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", ipcid='" + ipcid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", hostname='" + hostname + '\'' +
                ", sabsolutepath='" + sabsolutepath + '\'' +
                ", babsolutepath='" + babsolutepath + '\'' +
                ", provinceid='" + provinceid + '\'' +
                ", cityid='" + cityid + '\'' +
                ", regionid='" + regionid + '\'' +
                ", communityid='" + communityid + '\'' +
                '}';
    }
}
