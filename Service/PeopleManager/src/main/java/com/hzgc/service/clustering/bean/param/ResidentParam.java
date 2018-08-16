package com.hzgc.service.clustering.bean.param;

import com.hzgc.jni.PictureData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 常驻人口对象信息
 */
@ApiModel(value = "对象信息封装类")
@Data
@Slf4j
@ToString
public class ResidentParam implements Serializable{

    @ApiModelProperty(value = "数据库中唯一标识")
    private  String id;                                 // 数据库中的唯一标志    必填选项（update）
    /*添加、修改对象信息--前端入参*/
    @ApiModelProperty(value = "对象名字")
    private String name;                               // 对象名字
    @ApiModelProperty(value = "身份证")
    private String idcard;                            // 身份证
    @ApiModelProperty(value = "所属区域")
    private String regionID;                            //人员所在区域
    @ApiModelProperty(value = "性别")
    private Integer sex;                              // 性别 [0 = 未知（默认选项），1 = 男，2 = 女]
    @ApiModelProperty(value = "照片数据")
    private PictureData pictureDatas;                // 照片数据             必填选项（add）
    @ApiModelProperty(value = "布控理由")
    private String reason;                            // 布控理由
    @ApiModelProperty(value = "创建人")
    private String creator;                           // 创建人
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorContactWay;                // 布控人联系方式
//    private String createTime;                       // 创建时间
//    private String updateTime;                       // 更新时间
    @ApiModelProperty(value = "关注等级")
    private Integer followLevel;                     // 关注等级 关注等级 [0 = 非重点关注（默认选项），1 = 重点关注]
    @ApiModelProperty(value = "人员状态")
    private Integer status;                          // 人员状态 [0 = 常住人口（默认选项），1 = 建议迁出]
    @ApiModelProperty(value = "关爱等级")
    private Integer careLevel;                       // 关爱等级 关爱等级 [0 = 非关爱（默认选项），1 = 关爱]
    @ApiModelProperty(value = "地址")
    private String location;                        // 地址

}
