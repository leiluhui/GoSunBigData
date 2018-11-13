package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.People;

import java.util.List;

public interface PeopleMapper {
    int updateByPrimaryKeySelective(People record);
    int updateByPrimaryKey(People record);
    List<People> selectByPrimaryKey(String id);
}