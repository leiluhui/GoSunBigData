package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchName;

import java.util.List;

public interface DispatchNameMapper {
    int insert(DispatchName record);

    int insertSelective(DispatchName record);

    List<DispatchName> selectById(String defid);
}