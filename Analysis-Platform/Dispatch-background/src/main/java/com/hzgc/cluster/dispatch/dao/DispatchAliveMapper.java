package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchAlive;

import java.util.List;

public interface DispatchAliveMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchAlive record);

    int insertSelective(DispatchAlive record);

    DispatchAlive selectByPrimaryKey(String id);

    List<DispatchAlive> selectAll();

    int updateByPrimaryKeySelective(DispatchAlive record);

    int updateByPrimaryKeyWithBLOBs(DispatchAlive record);

    int updateByPrimaryKey(DispatchAlive record);
}