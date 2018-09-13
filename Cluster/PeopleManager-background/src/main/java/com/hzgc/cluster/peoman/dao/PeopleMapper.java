package com.hzgc.cluster.peoman.dao;

import com.hzgc.cluster.peoman.model.People;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PeopleMapper {

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(People record);

    int updateByPrimaryKey(People record);
}