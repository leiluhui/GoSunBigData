package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilterField implements Serializable {
    private Long regionid;      //区域ID
    private Long communityid;   // 小区ID
    private String name;        // 姓名
    private String idcard;      // 身份证
    private String imsi;        // IMSI码
    private String phone;       // 手机号
    private List<String> peopleIds;     // 人员ID列表
    private int start;                  // 起始行数
    private int limit;                  // 分页行数

    public static FilterField SearchParamShift(SearchPeopleDTO param) {
        FilterField field = new FilterField();
        if (param != null){
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
            field.setStart(param.getStart());
            field.setLimit(param.getLimit());
        }
        return field;
    }
}