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
    @ApiModelProperty(value = "数据库中的唯一标志")          //数据库中的唯一标志
    private String objectID;
    @ApiModelProperty(value = "区域id")                      //区域id
    private String regionId;
    @ApiModelProperty(value = "区域名称")                    //区域名称
    private String regionName;
    @ApiModelProperty(value = "对象名称")                    //对象名称
    private String name;
    @ApiModelProperty(value = "性别")                        //性别
    private int sex;
    @ApiModelProperty(value = "身份证号")                    //身份证号
    private String idcard;
    @ApiModelProperty(value = "创建者")                      //创建者
    private String creator;
    @ApiModelProperty(value = "创建者手机号")                //创建者手机号
    private String creatorConractWay;
    @ApiModelProperty(value = "创建时间")                    //创建时间
    private String createTime;
    @ApiModelProperty(value = "布控理由")                    //布控理由
    private String reason;
    @ApiModelProperty(value = "0：非重点关注，1：重点关注")  //0  非重点关注人口 1重点关注人口
    private int followLevel;
    @ApiModelProperty(value = "0,非关爱关注，1，关爱")       //0  非关爱人口    1关爱人口
    private int careLevel;
    @ApiModelProperty(value = "人员状态")
    private int status;
    @ApiModelProperty(value = "相似度")                      //相似度
    private float similarity;
    @ApiModelProperty(value = "地址")                        //地址
    private String location;

    public static PersonObject builder() {
        return new PersonObject();
    }
}
