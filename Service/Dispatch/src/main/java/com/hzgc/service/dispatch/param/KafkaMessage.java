package com.hzgc.service.dispatch.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class KafkaMessage implements Serializable {
    private String id;
    private Long regionId;
    private String bitFeature;
    private String car;
    private String mac;
}
