package com.hzgc.cluster.peoman.worker.service;

import lombok.Data;

@Data
class ComparePicture {
    private Long id;
    private Integer index;
    private String peopleId;
    private byte[] bitFeature;
}
