package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.People;

public interface PeopleMapper {

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(People record);

    int updateByPrimaryKey(People record);
}