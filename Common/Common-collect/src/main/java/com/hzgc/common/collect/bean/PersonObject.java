package com.hzgc.common.collect.bean;

import com.hzgc.seemmo.bean.personbean.Person;

import java.io.Serializable;

public class PersonObject implements Serializable {
    private String ipcId;               // 设备ID
    private String timeStamp;           // 时间（格式：2017-01-01 00：00：00）
    private String date;                // 日期（格式：2017-01-01）
    private int timeSlot;               // 时间段（格式：0000）（小时+分钟）
    private Person attribute;           // 行人属性对象
    private String surl;                // 小图ftp路径（带hostname的ftpurl）
    private String burl;                // 大图ftp路径（带hostname的ftpurl）
    private String relativePath;        // 小图相对路径（不带ftp根跟路径）
    private String relativePath_big;    // 大图相对路径（不带ftp根跟路径）
    private String ip;                  // 图片保存主机:ip
    private String hostname;            // 图片保存主机:hostname

    public static PersonObject builder() {
        return new PersonObject();
    }

    public String getIpcId() {
        return ipcId;
    }

    public PersonObject setIpcId(String ipcId) {
        this.ipcId = ipcId;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public PersonObject setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getDate() {
        return date;
    }

    public PersonObject setDate(String date) {
        this.date = date;
        return this;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public PersonObject setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public Person getAttribute() {
        return attribute;
    }

    public PersonObject setAttribute(Person attribute) {
        this.attribute = attribute;
        return this;
    }

    public String getSurl() {
        return surl;
    }

    public PersonObject setSurl(String surl) {
        this.surl = surl;
        return this;
    }

    public String getBurl() {
        return burl;
    }

    public PersonObject setBurl(String burl) {
        this.burl = burl;
        return this;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public PersonObject setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public String getRelativePath_big() {
        return relativePath_big;
    }

    public PersonObject setRelativePath_big(String relativePath_big) {
        this.relativePath_big = relativePath_big;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public PersonObject setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public PersonObject setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
}
