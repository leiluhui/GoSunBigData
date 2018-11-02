package com.hzgc.service.white.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "人员信息")
public class WhiteName implements Serializable {
    @ApiModelProperty(value = "照片ID")
    private String id;

    @ApiModelProperty(value = "照片")
    private String picture;

    @ApiModelProperty(value = "姓名")
    private String name;

    public  WhiteName(){}

    public WhiteName(String id, String picture, String name) {
        this.id = id;
        this.picture = picture;
        this.name = name;
    }
}
