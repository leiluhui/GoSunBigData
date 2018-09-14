package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel(value = "人口库查询参数封装类")
@Data
public class SearchParam implements Serializable {
    @ApiModelProperty(value = "查询类型")
    private Long searchType ;   // 0 :姓名 1：身份证 2 ： IMSI 3 : 手机号
    @ApiModelProperty(value = "用户输入的值")
    private String searchVal ;  // 用户输入的值
    @ApiModelProperty(value ="省市区选择器")
    private Long regionId ;     // 省市区选择器ID
    @ApiModelProperty(value="小区选择")
    private Long communityId;   // 小区
    @ApiModelProperty(value = "起始行数")
    private int start;          // 起始行数
    @ApiModelProperty(value = "分页行数")
    private int limit;          // 分页行数
}

