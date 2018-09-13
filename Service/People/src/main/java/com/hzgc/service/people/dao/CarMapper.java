package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Car;

import java.util.List;

public interface CarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    List<Car> selectByPeopleId(String peopleid);
}