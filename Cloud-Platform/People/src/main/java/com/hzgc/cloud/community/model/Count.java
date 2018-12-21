package com.hzgc.cloud.community.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "网格人口数量统计出参")
public class Count implements Serializable {
    @ApiModelProperty(value = "标签")
    private String flag;
    @ApiModelProperty(value = "人数")
    private int count;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
