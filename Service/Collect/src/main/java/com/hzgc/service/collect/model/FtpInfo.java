package com.hzgc.service.collect.model;

public class FtpInfo {
    private String ftpIp;

    private String proxyIp;

    private String proxyPort;

    private String ftpAccount;

    private String ftpPassword;

    private String ftpHome;

    private String ftpPort;

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp == null ? null : ftpIp.trim();
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp == null ? null : proxyIp.trim();
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort == null ? null : proxyPort.trim();
    }

    public String getFtpAccount() {
        return ftpAccount;
    }

    public void setFtpAccount(String ftpAccount) {
        this.ftpAccount = ftpAccount == null ? null : ftpAccount.trim();
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword == null ? null : ftpPassword.trim();
    }

    public String getFtpHome() {
        return ftpHome;
    }

    public void setFtpHome(String ftpHome) {
        this.ftpHome = ftpHome == null ? null : ftpHome.trim();
    }

    public String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort == null ? null : ftpPort.trim();
    }
}