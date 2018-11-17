package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "重点人员识别记录查询出参")
@Data
public class ImportantRecognizeVO implements Serializable {
    @ApiModelProperty(value = "重点人员抓拍记录")
    private List<ImportantPeopleRecognizeVO> importantPeopleCaptureList;
    @ApiModelProperty(value = "重点人员识别记录")
    private List<ImportantPeopleRecognizeVO> importantPeopleRecognizeList;
    @ApiModelProperty(value = "记录总数")
    private int totalNum;
}
