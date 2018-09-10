package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Community;

public interface CommunityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Community record);

    int insertSelective(Community record);

    Community selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Community record);

    int updateByPrimaryKey(Community record);
}