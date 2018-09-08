package com.hzgc.service.facedispatch.starepo.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 以图搜图:单个对象信息封装类
 */
@ApiModel(value = "单个对象信息封装类")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PersonObject implements Serializable {
    @ApiModelProperty(value = "对象ID")
    private String objectID;

    @ApiModelProperty(value = "对象名称")
    private String name;

    @ApiModelProperty(value = "对象类型")
    private String objectTypeKey;

    @ApiModelProperty(value = "对象类型名称")
    private String objectTypeName;

    @ApiModelProperty(value = "性别")
    private int sex;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建者手机号")

    private String creatorConractWay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "布控理由")
    private String reason;

    @ApiModelProperty(value = "相似度")
    private float similarity;
}
