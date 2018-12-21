package com.hzgc.cloud.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "实有,重点,关爱,新增,迁出人口数量统计封装类")
@Data
public class CountVO implements Serializable {
    @ApiModelProperty(value = "网格人员数量")
    private int People;
    @ApiModelProperty(value = "矫正人员数量")
    private int rectification ;
    @ApiModelProperty(value = "刑释解戒数量")
    private int criminal;
    @ApiModelProperty(value = "精神病人数量")
    private int mental_patient ;
    @ApiModelProperty(value = "吸毒人员数量")
    private int drug_addict;
    @ApiModelProperty(value = "境外人员数量")
    private int overseas;
    @ApiModelProperty(value = "艾滋病人数量")
    private int HIV_infected;
    @ApiModelProperty(value = "重点青少年数量")
    private int important_adolescent;
    @ApiModelProperty(value = "留守人员数量")
    private int rusu;
    @ApiModelProperty(value = "涉军人员数量")
    private int military_related;
    @ApiModelProperty(value = "信访人员数量")
    private int petition_letter;
    @ApiModelProperty(value = "邪教人员数量")
    private int heresy;
}
