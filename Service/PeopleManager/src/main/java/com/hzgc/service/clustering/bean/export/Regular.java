package com.hzgc.service.clustering.bean.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Regular implements Serializable{

    @ApiModelProperty(value = "区域ID")
    private String regionID;

    @ApiModelProperty(value = "区域名称")
    private String regionName;

    @ApiModelProperty(value = "相似度sim")
    private String sim;

   @ApiModelProperty(value = "迁入持续出现次数")
    private String MoveInCount;

   @ApiModelProperty(value = "迁出持续离线天数")
    private String MoveOutDays;
}
