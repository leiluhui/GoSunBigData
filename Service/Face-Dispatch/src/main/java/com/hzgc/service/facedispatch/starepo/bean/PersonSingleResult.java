package com.hzgc.service.facedispatch.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 以图搜图:多个人的的情况下，每个图片对应的结果集
 */
@ApiModel(value = "多个人的的情况下，每个图片对应的结果集")
@Data
public class PersonSingleResult implements Serializable {
    @ApiModelProperty(value = "子搜索Id")
    private String searchId;

    @ApiModelProperty(value = "搜索的总数")
    private int total;

    @ApiModelProperty(value = "搜索图片ID")
    private List<String> imageNames;

    @ApiModelProperty(value = "不用聚类的时候的返回结果")
    private List<PersonObject> objectInfoBeans;

    @ApiModelProperty(value = "根据 pkey 分类后的返回结果")
    private List<PersonObjectGroupByPkey> singleObjKeyResults;
}
