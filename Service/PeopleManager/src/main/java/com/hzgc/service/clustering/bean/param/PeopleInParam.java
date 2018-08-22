package com.hzgc.service.clustering.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

@ApiModel(value = "建议迁入人口入参")
@Data
@ToString
public class PeopleInParam implements Serializable {
    @ApiModelProperty(value = "时间月份(yyyyMM)")
    private String yearMonth;

    @ApiModelProperty(value = "区域ID")
    private String region;

    @ApiModelProperty(value = "聚类ID")
    private String clusterId;

    @ApiModelProperty(value = "忽略聚类(yes or no)")
    private String flag;

    @ApiModelProperty(value = "分页起始位置")
    private int start;

    @ApiModelProperty(value = "分页每页条数")
    private int limit;

    @ApiModelProperty(value = "排序参数")
    private String sortParam;
}
