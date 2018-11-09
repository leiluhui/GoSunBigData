package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.Car;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper {
    Car selectByCar(String car);
}