package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.House;

public interface HouseMapper {

    int insert(House record);

    int insertSelective(House record);

    House selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(House record);

    int updateByPrimaryKey(House record);
}