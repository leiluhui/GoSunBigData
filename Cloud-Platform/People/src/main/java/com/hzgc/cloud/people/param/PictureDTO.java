package com.hzgc.cloud.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "添加,修改人口库照片信息封装类")
@Data
public class PictureDTO implements Serializable {
    @ApiModelProperty(value = "照片类型(0:证件照片,1:实采照片)")
    private int type;
    @ApiModelProperty(value = "照片ID(修改时使用)")
    private Long pictureId;
    @ApiModelProperty(value = "人员全局ID")
    private String peopleId;
    @ApiModelProperty(value = "照片数据")
    private String picture;
}
