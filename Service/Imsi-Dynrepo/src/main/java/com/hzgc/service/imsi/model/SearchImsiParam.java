package com.hzgc.service.imsi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchImsiParam implements Serializable {
    private int searchType; // 查询类型（0：设备ID，1：imsi）
    private String searchVal; // 查询内容
    private List<Long> communityIds;
}
