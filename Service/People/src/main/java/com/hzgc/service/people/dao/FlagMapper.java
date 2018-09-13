package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Flag;

import java.util.List;

public interface FlagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Flag record);

    int insertSelective(Flag record);

    Flag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Flag record);

    int updateByPrimaryKey(Flag record);

    List<Flag> selectByPeopleId(String peopleid);
}