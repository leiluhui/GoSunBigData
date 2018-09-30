package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResult implements Serializable {

    //总搜索ID
    private String searchId;

    //设备总数
    private int deivceCount;

    //子结果集集合
    private List<SingleSearchResult> singleResults;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

