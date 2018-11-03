package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchDefinition;

import java.util.List;

public interface DispatchDefinitionMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchDefinition record);

    int insertSelective(DispatchDefinition record);

    DispatchDefinition selectByPrimaryKey(String id);

    List<DispatchDefinition> selectAll();

    int updateByPrimaryKeySelective(DispatchDefinition record);

    int updateByPrimaryKey(DispatchDefinition record);
}