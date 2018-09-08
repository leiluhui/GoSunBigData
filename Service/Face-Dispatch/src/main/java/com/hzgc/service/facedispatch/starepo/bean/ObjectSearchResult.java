package com.hzgc.service.facedispatch.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 以图搜图:返回结果封装类
 */
@ApiModel(value = "以图搜图返回结果封装类")
@Data
public class ObjectSearchResult implements Serializable {

    @ApiModelProperty(value = "总的searchId")
    private String searchId;

    @ApiModelProperty(value = "最终需要返回的结果，String是分别的Id")
    private List<PersonSingleResult> singleSearchResults;
}
