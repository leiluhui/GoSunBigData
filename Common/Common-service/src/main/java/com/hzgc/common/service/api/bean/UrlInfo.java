package com.hzgc.common.service.api.bean;

import lombok.Data;

@Data
public class UrlInfo {
    private String hostname;
    private String ip;
    private String http_hostname;
    private String http_ip;
    private String port = "2573";
}
