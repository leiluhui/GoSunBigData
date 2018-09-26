package com.hzgc.common.service.peoman;

import lombok.Data;

@Data
public class SyncPeopleManager {
    /**
     * 必填
     * 2:人员添加
     */
    private String type;
    //必填
    private String personid;
    //可选
    private String bitFeature;
    //可选
    private Long pictureId;

}
