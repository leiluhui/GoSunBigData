package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.People;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PeopleMapper {

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(People record);

    int updateByPrimaryKey(People record);
}