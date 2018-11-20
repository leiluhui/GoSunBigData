package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Car;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface CarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    List<Car> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);

    int delete(String peopleid);
}