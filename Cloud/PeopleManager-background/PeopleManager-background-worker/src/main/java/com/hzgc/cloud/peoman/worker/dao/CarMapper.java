package com.hzgc.cloud.peoman.worker.dao;

import com.hzgc.cloud.peoman.worker.model.Car;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper {
    Car selectByCar(String car);
}