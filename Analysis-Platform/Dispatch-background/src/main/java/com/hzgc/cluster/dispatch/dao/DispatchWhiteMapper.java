package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchWhite;

import java.util.List;

public interface DispatchWhiteMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchWhite record);

    int insertSelective(DispatchWhite record);

    DispatchWhite selectByPrimaryKey(String id);

    List<DispatchWhite> selectAll();

    int updateByPrimaryKeySelective(DispatchWhite record);

    int updateByPrimaryKeyWithBLOBs(DispatchWhite record);

    int updateByPrimaryKey(DispatchWhite record);
}