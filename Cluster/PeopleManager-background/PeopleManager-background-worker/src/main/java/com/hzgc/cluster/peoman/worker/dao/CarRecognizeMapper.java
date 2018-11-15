package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.CarRecognize;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarRecognizeMapper {
    int deleteByPrimaryKey(String id);

    int insert(CarRecognize record);

    int insertSelective(CarRecognize record);

    CarRecognize selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CarRecognize record);
}