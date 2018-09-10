package com.hzgc.service.dyncar.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchResult implements Serializable {

    //总搜索ID
    private String searchId;

    //子结果集集合
    private SingleSearchResult singleSearchResult;

}

