package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 常驻人口库查询的时候返回的结果
 */
@ApiModel(value = "常驻人口库查询的时候返回的结果")
@Data
@ToString
public class ResidentSearchResult implements Serializable{

    /**
     * 不用分类的时候的返回结果
     */
    @ApiModelProperty(value = "不用分类的时候的返回结果")
    private List<PersonObject> objectInfoBeans;
}
