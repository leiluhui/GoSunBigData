package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Flag;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface FlagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Flag record);

    int insertSelective(Flag record);

    Flag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Flag record);

    int updateByPrimaryKey(Flag record);

    List<Flag> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);
}