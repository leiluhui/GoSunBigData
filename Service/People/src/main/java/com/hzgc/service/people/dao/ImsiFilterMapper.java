package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.ImsiFilter;

public interface ImsiFilterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImsiFilter record);

    int insertSelective(ImsiFilter record);

    ImsiFilter selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImsiFilter record);

    int updateByPrimaryKey(ImsiFilter record);
}