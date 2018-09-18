package com.hzgc.service.facedispatch.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 以图搜图:按照对象类型分类，排序返回
 */
@ApiModel(value = "按照对象类型分类，排序返回")
@Data
public class PersonObjectGroupByPkey implements Serializable {
    @ApiModelProperty(value = "对象类型Key")
    private String objectTypeKey;

    @ApiModelProperty(value = "对象类型名称")
    private String objectTypeName;

    @ApiModelProperty(value = "底库信息")
    private List<PersonObject> personObjectList;

    @ApiModelProperty(value = "当前对象类型下的总人数")
    private int total;
}
