package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.Dispatch;

import java.util.List;

public interface DispatchMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dispatch record);

    int insertSelective(Dispatch record);

    Dispatch selectByPrimaryKey(String id);

    List<Dispatch> selectAll();

    List<Dispatch> selectByIds(List<String> ids);

    int updateByPrimaryKeySelective(Dispatch record);

    int updateByPrimaryKeyWithBLOBs(Dispatch record);

    int updateByPrimaryKey(Dispatch record);
}