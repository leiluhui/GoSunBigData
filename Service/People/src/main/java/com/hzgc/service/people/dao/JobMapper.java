package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Job;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobMapper {

    int insert(Job record);

    int insertSelective(Job record);

    Job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Job record);

    int updateByPrimaryKey(Job record);
}