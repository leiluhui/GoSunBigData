package com.hzgc.cluster.dispach.dao;

import com.hzgc.cluster.dispach.model.Dispach;

import java.util.List;

public interface DispachMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dispach record);

    int insertSelective(Dispach record);

    Dispach selectByPrimaryKey(String id);

    List<Dispach> selectAll();

    List<Dispach> selectByIds(List<String> ids);

    int updateByPrimaryKeySelective(Dispach record);

    int updateByPrimaryKeyWithBLOBs(Dispach record);

    int updateByPrimaryKey(Dispach record);
}