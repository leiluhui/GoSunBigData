package com.hzgc.charts.service;

import com.hzgc.charts.domain.Car;

import java.util.List;
/**
 * created by liang on 2018/12/12
 */
public interface CarService {

    /**
     * 查询所有的car信息
     */
    public List<Car> findAll();

    /**
     * 查询所有的car的数量
     */
    long findTotalNum();

    /**
     * 查询今天的car的数量
     */
    long findTodayNum();
}
