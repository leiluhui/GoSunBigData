package com.hzgc.datasyncer.bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface DocumentInfo {
    String FACE_INDEX_NAME = "dynamicface";
    String FACE_TYPE = "face";
    String PERSON_INDEX_NAME = "dynamicperson";
    String PERSON_TYPE = "person";
    String CAR_INDEX_NAME = "dynamiccar";
    String CAR_TYPE = "car";
}
