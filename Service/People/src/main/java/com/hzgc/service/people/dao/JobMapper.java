package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Job;

public interface JobMapper {

    int insert(Job record);

    int insertSelective(Job record);

    Job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Job record);

    int updateByPrimaryKey(Job record);
}