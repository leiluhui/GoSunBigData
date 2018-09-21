package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.Count24Hour;

public interface Count24HourMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Count24Hour record);

    int insertSelective(Count24Hour record);

    Count24Hour selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Count24Hour record);

    int updateByPrimaryKey(Count24Hour record);
}