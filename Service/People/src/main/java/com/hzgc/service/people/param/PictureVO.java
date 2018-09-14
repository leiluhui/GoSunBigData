package com.hzgc.service.people.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "根据ID或人员ID查询，后台照片信息封装类")
@Data
public class PictureVO implements Serializable {
    @ApiModelProperty(value ="照片ID列表")
    private List<Long> pictureIds;
    @ApiModelProperty(value ="证件照片列表")
    private List<byte[]> idcardPics;
    @ApiModelProperty(value ="实采照片列表")
    private List<byte[]> capturePics;
}
