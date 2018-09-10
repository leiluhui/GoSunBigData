package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Recognize;

public interface RecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Recognize record);

    int insertSelective(Recognize record);

    Recognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Recognize record);

    int updateByPrimaryKey(Recognize record);
}