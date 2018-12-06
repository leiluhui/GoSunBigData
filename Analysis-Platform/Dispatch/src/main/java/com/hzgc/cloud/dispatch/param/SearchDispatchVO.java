package com.hzgc.cloud.dispatch.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "布控库模糊查询出参")
@Data
public class SearchDispatchVO implements Serializable {
    @ApiModelProperty(value = "总条数")
    private int total;
    @ApiModelProperty(value = "布控人员信息列表")
    private List<DispatchVO> peopleVOList;
}
