package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Flag;

public interface FlagMapper {

    int insert(Flag record);

    int insertSelective(Flag record);

    Flag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Flag record);

    int updateByPrimaryKey(Flag record);
}