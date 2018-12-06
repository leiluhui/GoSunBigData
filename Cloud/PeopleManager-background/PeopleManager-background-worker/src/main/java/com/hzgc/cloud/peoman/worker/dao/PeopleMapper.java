package com.hzgc.cloud.peoman.worker.dao;

import com.hzgc.cloud.peoman.worker.model.People;

import java.util.List;

public interface PeopleMapper {
    int updateByPrimaryKeySelective(People record);
    int updateByPrimaryKey(People record);
    List<People> selectByPrimaryKey(String id);
}