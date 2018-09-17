package com.hzgc.cluster.peoman.worker.service;

import lombok.Data;

@Data
public class ComparePicture {
    private Long id;
    private String peopleId;
    private byte[] bitFeature;
    private String community;
}
