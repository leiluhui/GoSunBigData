package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Car;

public interface CarMapper {

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);
}