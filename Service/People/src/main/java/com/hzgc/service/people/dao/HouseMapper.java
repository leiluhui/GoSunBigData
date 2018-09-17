package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.House;

import java.util.List;

public interface HouseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(House record);

    int insertSelective(House record);

    House selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(House record);

    int updateByPrimaryKey(House record);

    List<House> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);
}