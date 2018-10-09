package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Imsi;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface ImsiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Imsi record);

    int insertSelective(Imsi record);

    Imsi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Imsi record);

    int updateByPrimaryKey(Imsi record);

    List<Imsi> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);
}