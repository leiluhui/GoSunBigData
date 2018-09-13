package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchParam implements Serializable {
    private Long searchType ;   // 0 :姓名 1：身份证 2 ： IMSI 3 : 手机号
    private String searchVal ;  // 用户输入的值
    private Long regionId ;     // 省市区选择器ID
    private Long communityId;   // 小区
    private int start;          // 起始行数
    private int limit;          // 分页行数
}

