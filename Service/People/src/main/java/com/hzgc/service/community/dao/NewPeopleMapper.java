package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.NewPeople;

public interface NewPeopleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewPeople record);

    int insertSelective(NewPeople record);

    NewPeople selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewPeople record);

    int updateByPrimaryKey(NewPeople record);
}