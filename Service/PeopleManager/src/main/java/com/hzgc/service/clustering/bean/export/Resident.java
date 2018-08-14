package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对象信息部分字段（除了pictureData字段）
 */
@Data
public class Resident {
    /*根据id获取对象信息——后台返回封装*/
    @ApiModelProperty(value = "对象名字")
    private String name;                               // 对象名字
    @ApiModelProperty(value = "身份证")
    private String idcard;                            // 身份证
    @ApiModelProperty(value = "区域名字")
    private String regionName;                        //对象所属区域名称
    @ApiModelProperty(value = "性别")
    private Integer sex;                              // 性别 [0 = 未知（默认选项），1 = 男，2 = 女]
    @ApiModelProperty(value = "布控理由")
    private String reason;                            // 布控理由
    @ApiModelProperty(value = "创建人")
    private String creator;                           // 创建人
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorContactWay;                // 布控人联系方式
    @ApiModelProperty(value = "创建时间")
    private String createTime;                       // 创建时间
    @ApiModelProperty(value = "更新时间")
    private String updateTime;                       // 更新时间
    @ApiModelProperty(value = "关注等级")
    private Integer followLevel;                     // 关注等级 关注等级 [0 = 非重点关注（默认选项），1 = 重点关注]
    @ApiModelProperty(value = "人员状态")
    private Integer status;                          // 人员状态 [0 = 常住人口（默认选项），1 = 建议迁出]
    @ApiModelProperty(value = "关爱等级")
    private Integer careLevel;                       //关爱等级  [0 = 非关爱人口（默认选项），1=关爱人口]
}
