package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 按照区域分类，排序返回
 */
@ApiModel(value = "按照区域分类，排序返回")
@Data
@ToString
public class ResidentGroupByRegion {

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String regionId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;

    /**
     * 常驻人口库信息
     */
    @ApiModelProperty(value = "常驻人口库信息")
    private List<PersonObject> personObjectList;

    /**
     * 当前区域中人的总数
     */
    @ApiModelProperty(value = "当前区域中人的总数")
    private int total;
}
