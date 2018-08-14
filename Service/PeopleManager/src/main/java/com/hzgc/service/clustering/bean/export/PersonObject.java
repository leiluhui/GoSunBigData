package com.hzgc.service.clustering.bean.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 常驻人口库每个人的信息
 */
@ApiModel(value = "常驻人口库每个人的信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonObject implements Serializable{
    /**
     * 数据库中的唯一标志
     */
    @ApiModelProperty(value = "数据库中的唯一标志")
    private String objectID;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String regionId;

    /**
     * 对象类型名称
     */
    @ApiModelProperty(value = "对象类型名称")
    private String objectTypeName;

    /**
     * 对象名称
     */
    @ApiModelProperty(value = "对象名称")
    private String name;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private int sex;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idcard;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建者手机号
     */
    @ApiModelProperty(value = "创建者手机号")
    private String creatorConractWay;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 布控理由
     */
    @ApiModelProperty(value = "布控理由")
    private String reason;

    /**
     * 0,非重点关注，1，重点关注
     */
    @ApiModelProperty(value = "0：非重点关注，1：重点关注")
    private int followLevel;


    /**
     * 0,非关爱关注，1，关爱
     */
    @ApiModelProperty(value = "0,非关爱关注，1，关爱")
    private int careLevel;
    /**
     * 相似度
     */
    @ApiModelProperty(value = "相似度")
    private float similarity;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String location;

    public static PersonObject builder() {
        return new PersonObject();
    }
}
