package com.hzgc.cluster.dispach.dao;

import com.hzgc.cluster.dispach.model.Dispach;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
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