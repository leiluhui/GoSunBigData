package com.hzgc.service.clustering.bean.param;

import com.hzgc.jni.PictureData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 获取对象信息查询参数
 */
@ApiModel(value = "对象信息查询封装类")
@Data
@ToString
public class GetResidentParam implements Serializable {

    @ApiModelProperty(value = "图片数据")
    private PictureData pictureData;       //照片数据
    @ApiModelProperty(value = "区域类型列表")
    private List<String> regionList;
    @ApiModelProperty(value = "对象名称")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idcard;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorContactWay;
    @ApiModelProperty(value = "相似度")
    private float similarity;
    @ApiModelProperty(value = "排序参数")
    private List<ResidentSortParam> sortParamList;  // 排序参数
    @ApiModelProperty(value = "人员状态")
    private Integer status;                       // 人员状态 [0 = 常住人口（默认选项），1 = 建议迁出]
    @ApiModelProperty(value = "关注等级")
    private Integer followLevel;                  // 关注等级 [1 = 非重点关注（默认选项），2 = 重点关注]
    @ApiModelProperty(value = "关爱等级")
    private Integer careLevel;                    //关爱等级  [1 = 非关爱人口（默认选项），2 = 关爱人口]
    @ApiModelProperty(value = "起始行数")
    private Integer start;                        // 起始行数
    @ApiModelProperty(value = "分页行数")
    private Integer limit;                        // 分页行数
    @ApiModelProperty(value = "地址")
    private String location;                      // 地址
}
