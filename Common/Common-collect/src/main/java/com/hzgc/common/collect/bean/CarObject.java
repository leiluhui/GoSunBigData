package com.hzgc.common.collect.bean;

import com.hzgc.seemmo.bean.carbean.Vehicle;

import java.io.Serializable;

public class CarObject implements Serializable {
    private String ipcId;               // 设备ID
    private String timeStamp;           // 时间（格式：2017-01-01 00：00：00）
    private String date;                // 日期（格式：2017-01-01）
    private int timeSlot;               // 时间段（格式：0000）（小时+分钟）
    private Vehicle attribute;          // 车辆属性对象
    private String surl;                // 小图ftp路径（带hostname的ftpurl）
    private String burl;                // 大图ftp路径（带hostname的ftpurl）
    private String relativePath;        // 小图相对路径（不带ftp根跟路径）
    private String relativePath_big;    // 大图相对路径（不带ftp根跟路径）
    private String ip;                  // 图片保存主机:ip
    private String hostname;            // 图片保存主机:hostname

    public static CarObject builder() {
        return new CarObject();
    }

    public String getIpcId() {
        return ipcId;
    }

    public CarObject setIpcId(String ipcId) {
        this.ipcId = ipcId;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public CarObject setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getDate() {
        return date;
    }

    public CarObject setDate(String date) {
        this.date = date;
        return this;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public CarObject setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public Vehicle getAttribute() {
        return attribute;
    }

    public CarObject setAttribute(Vehicle attribute) {
        this.attribute = attribute;
        return this;
    }

    public String getSurl() {
        return surl;
    }

    public CarObject setSurl(String surl) {
        this.surl = surl;
        return this;
    }

    public String getBurl() {
        return burl;
    }

    public CarObject setBurl(String burl) {
        this.burl = burl;
        return this;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public CarObject setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public String getRelativePath_big() {
        return relativePath_big;
    }

    public CarObject setRelativePath_big(String relativePath_big) {
        this.relativePath_big = relativePath_big;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public CarObject setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public CarObject setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
}
