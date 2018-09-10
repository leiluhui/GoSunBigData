package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.NewPeople;

public interface NewPeopleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewPeople record);

    int insertSelective(NewPeople record);

    NewPeople selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewPeople record);

    int updateByPrimaryKey(NewPeople record);
}