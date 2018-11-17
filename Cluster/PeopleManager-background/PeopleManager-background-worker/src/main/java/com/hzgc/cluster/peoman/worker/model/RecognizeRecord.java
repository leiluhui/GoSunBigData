package com.hzgc.cluster.peoman.worker.model;

import lombok.Data;

import java.util.Date;

@Data
public class RecognizeRecord {
    private String id;
    private String peopleid;
    private Long community;
    private Long pictureid;
    private String deviceid;
    private Date capturetime;
    private String surl;
    private String burl;
    private Integer flag;
    private Float similarity;
    private String plate;
    private String imsi;
    private String mac;
    private Integer type;
    private Integer filterTime;
}