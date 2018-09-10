package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.FilterField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PeopleMapper {

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(People record);

    int updateByPrimaryKey(People record);

    List<People> searchPeople(FilterField field);
}