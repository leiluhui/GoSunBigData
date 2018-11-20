package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.House;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface HouseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(House record);

    int insertSelective(House record);

    House selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(House record);

    int updateByPrimaryKey(House record);

    List<House> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);

    int delete(String peopleid);
}