package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchName;

import java.util.List;

public interface DispatchNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DispatchName record);

    int insertSelective(DispatchName record);

    DispatchName selectByPrimaryKey(Integer id);

    List<DispatchName> selectByDefid(String defid);

    int updateByPrimaryKeySelective(DispatchName record);

    int updateByPrimaryKeyWithBLOBs(DispatchName record);

    int updateByPrimaryKey(DispatchName record);
}