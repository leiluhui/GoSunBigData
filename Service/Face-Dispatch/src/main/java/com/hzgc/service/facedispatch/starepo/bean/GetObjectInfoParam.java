package com.hzgc.service.facedispatch.starepo.bean;

import com.hzgc.jniface.PictureData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 以图搜图:查询参数封装类
 */
@ApiModel(value = "以图搜图查询参数封装类")
@Data
public class GetObjectInfoParam implements Serializable {
    @ApiModelProperty(value = "图片数据")
    private List<PictureData> pictureDataList;    // 照片数据
    @ApiModelProperty(value = "对象类型列表")
    private List<String> objectTypeKeyList;       // 对象类型列表
    @ApiModelProperty(value = "对象名字")
    private String objectName;                    // 对象名字
    @ApiModelProperty(value = "身份证")
    private String idcard;                        // 身份证
    @ApiModelProperty(value = "性别")
    private Integer sex;                          // 性别            [0 = 未知（默认选项），1 = 男，2 = 女]
    @ApiModelProperty(value = "创建人")
    private String creator;                       // 创建人
    @ApiModelProperty(value = "创建人联系方式")
    private String creatorConractWay;             // 布控人联系方式
    @ApiModelProperty(value = "相似度")
    private float similarity;                     // 相似度           [与 pictureDataList 共同存在 ]
    @ApiModelProperty(value = "是否是同一个人")
    private boolean singlePerson;                 // 是否是同一个人
    @ApiModelProperty(value = "排序参数")
    private List<StaticSortParam> sortParamList;  // 排序参数
    @ApiModelProperty(value = "起始行数")
    private Integer start;                        // 起始行数
    @ApiModelProperty(value = "分页行数")
    private Integer limit;                        // 分页行数
}
