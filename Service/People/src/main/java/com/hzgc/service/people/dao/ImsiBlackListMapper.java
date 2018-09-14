package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.ImsiBlackList;

public interface ImsiBlackListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImsiBlackList record);

    int insertSelective(ImsiBlackList record);

    ImsiBlackList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImsiBlackList record);

    int updateByPrimaryKey(ImsiBlackList record);
}