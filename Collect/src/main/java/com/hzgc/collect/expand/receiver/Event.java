package com.hzgc.collect.expand.receiver;

public class Event {
    private String ipcId;                // ipc id
    private String timeStamp;            // 抓拍时间 (2018-08-10 17:13:22)
    private String date;                 // 日期(2018-08-10)
    private int timeSlot;                // 时间段（格式：1713）（小时+分钟）
    private String ftpUrl_hostname;      // ftp url (带hostname的ftpUrl)
    private String ftpUrl_ip;            // ftp url (带ip的ftpUrl)
    private String fileAbsolutePath;     // 大图存储绝对路径(带ftp根目录)
    private String ftpAbsolutePath;      // 大图存储相对路径(不带ftp根目录)
    private String ip;                   // 图片保存主机:ip
    private String hostname;             // 图片保存主机:hostname

    public static Event builder(){
        return new Event();
    }

    public String getIpcId() {
        return ipcId;
    }

    public Event setIpcId(String ipcId) {
        this.ipcId = ipcId;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Event setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public Event setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public String getFtpUrl_hostname() {
        return ftpUrl_hostname;
    }

    public Event setFtpUrl_hostname(String ftpUrl_hostname) {
        this.ftpUrl_hostname = ftpUrl_hostname;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Event setDate(String date) {
        this.date = date;
        return this;
    }

    public String getFtpUrl_ip() {
        return ftpUrl_ip;
    }

    public Event setFtpUrl_ip(String ftpUrl_ip) {
        this.ftpUrl_ip = ftpUrl_ip;
        return this;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public Event setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
        return this;
    }

    public String getFtpAbsolutePath() {
        return ftpAbsolutePath;
    }

    public Event setFtpAbsolutePath(String ftpAbsolutePath) {
        this.ftpAbsolutePath = ftpAbsolutePath;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Event setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public Event setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
}
