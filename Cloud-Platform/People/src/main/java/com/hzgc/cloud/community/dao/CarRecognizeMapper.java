package com.hzgc.cloud.community.dao;

import com.hzgc.cloud.community.model.CarRecognize;

import java.util.List;

public interface CarRecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CarRecognize record);

    int insertSelective(CarRecognize record);

    CarRecognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CarRecognize record);

    int updateByPrimaryKey(CarRecognize record);

    List<CarRecognize> selectByPeopleId(String peopleid);
}