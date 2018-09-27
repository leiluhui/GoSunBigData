package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.People;

public interface PeopleMapper {
    int updateByPrimaryKeySelective(People record);
    int updateByPrimaryKey(People record);
}