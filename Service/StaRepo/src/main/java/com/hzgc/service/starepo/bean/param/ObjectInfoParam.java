package com.hzgc.service.starepo.bean.param;

import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.jni.PictureData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 对象信息
 */
@ApiModel(value = "对象信息封装类")
@Data
@Slf4j
@ToString
public class ObjectInfoParam implements Serializable {

    /* 数据操作 */
    @ApiModelProperty(value = "数据库ID")
    private String id;                          // 数据库中的唯一标志    必填选项（update）

    /* 添加、修改对象信息——前端入参 */
    @ApiModelProperty(value = "对象名字")
    private String name;          // 对象名字
    @ApiModelProperty(value = "对象类型key")
    private String objectTypeKey;               // 对象类型key          必填选项（add、update）
    @ApiModelProperty(value = "身份证")
    private String idcard;                      // 身份证
    @ApiModelProperty(value = "性别")
    private int sex;                            // 性别 [ 1:男，2:女 默认为0：未知]
    @ApiModelProperty(value = "照片数据")
    private PictureData pictureDatas;           // 照片数据             必填选项（add）
    @ApiModelProperty(value = "布控理由")
    private String reason;                      // 布控理由
    @ApiModelProperty(value = "创建人")
    private String creator;                     // 创建人
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorConractWay;           // 布控人联系方式
    @ApiModelProperty(value = "创建时间")
    private String createTime;                  // 创建时间
    @ApiModelProperty(value = "更新时间")
    private String updateTime;                  // 更新时间
    @ApiModelProperty(value = "关注等级")
    private int followLevel;                    // 关注等级 [ 0:非重点关注，1:重点关注，默认为0]     必填选项（add、update）
    @ApiModelProperty(value = "人员状态")
    private int status;                         // 人员状态 [ 0:常住人口，1:建议迁出，默认为0]
    @ApiModelProperty(value = "地址")
    private String location;                    // 地址
}
