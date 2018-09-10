package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilterField implements Serializable {
    private Long regionid;
    private Long communityid;
    private String name;
    private String idcard;
    private String imsi;
    private String phone;
    private List<String> peopleIds;

    public static FilterField SearchParamShift(SearchParam param) {
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