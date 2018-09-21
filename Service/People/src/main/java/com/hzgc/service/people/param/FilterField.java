package com.hzgc.service.people.param;

import com.hzgc.service.community.param.SearchParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "参数过滤封装")
@Data
public class FilterField implements Serializable {
    @ApiModelProperty(value = "区域ID（省市区选择器）")
    private Long regionid;
    @ApiModelProperty(value = "小区ID")
    private Long communityid;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idcard;
    @ApiModelProperty(value = "IMSI码")
    private String imsi;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "人员ID列表")
    private List<String> peopleIds;

    public static FilterField SearchParamShift(SearchParamDTO param) {
        FilterField field = new FilterField();
        field.setRegionid(param.getRegionId());
        field.setCommunityid(param.getCommunityId());
        if (param.getSearchType() == 0) {
            field.setName(param.getSearchVal());
        }
        if (param.getSearchType() == 1) {
            field.setIdcard(param.getSearchVal());
        }
        if (param.getSearchType() == 2) {
            field.setImsi(param.getSearchVal());
        }
        if (param.getSearchType() == 3) {
            field.setPhone(param.getSearchVal());
        }
        return field;
    }
}