package com.hzgc.cluster.peoman.worker.model;

import lombok.Data;

import java.util.Date;

@Data
public class PeopleRecognize {
    private Long id;
    private String peopleid;
    private Long community;
    private Long pictureid;
    private String deviceid;
    private Date capturetime;
    private String surl;
    private String burl;
    private Integer flag;
}