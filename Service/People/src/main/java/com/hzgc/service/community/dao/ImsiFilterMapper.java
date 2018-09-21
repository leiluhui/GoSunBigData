package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.ImsiFilter;

public interface ImsiFilterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImsiFilter record);

    int insertSelective(ImsiFilter record);

    ImsiFilter selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImsiFilter record);

    int updateByPrimaryKey(ImsiFilter record);
}