package com.hzgc.service.clustering.bean.export;

import com.hzgc.common.faceclustering.PeopleInAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "建议迁入人口出参")
@Data
@ToString
public class PeopleInResult implements Serializable {
    @ApiModelProperty(value = "统计人口总数")
    private int totalCount;
    @ApiModelProperty(value = "建议迁入人口列表")
    private List<PeopleInAttribute> peopleInAttributeList;
    @ApiModelProperty(value = "忽略人口列表")
    private List<PeopleInAttribute> peopleInAttributeList_ignore;
}
