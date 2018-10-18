package com.hzgc.service.dispatch.param;

import java.io.Serializable;

public class KafkaMessage implements Serializable {
    private String id;
    private Long regionId;
    private String bitFeature;
    private String car;
    private String mac;
}
