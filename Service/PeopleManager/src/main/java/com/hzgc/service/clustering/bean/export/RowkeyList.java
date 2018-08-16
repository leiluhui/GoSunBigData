package com.hzgc.service.clustering.bean.export;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RowkeyList implements Serializable{
    @ApiModelProperty(value = "rowkeylist")
    private List<String> rowkeyList;
}
