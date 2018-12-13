package com.hzgc.charts.service.impl;

import cn.hutool.core.date.DateUtil;
import com.hzgc.charts.dao.CarRepository;
import com.hzgc.charts.domain.Car;
import com.hzgc.charts.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by liang on 2018/12/12
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> findAll() {

        Iterable<Car> userIterable = carRepository.findAll();
        List<Car> list = new ArrayList<>();
        userIterable.forEach(single ->list.add(single));
        return list;
    }

    @Override
    public long findTotalNum() {
        return carRepository.count();
    }

    @Override
    public long findTodayNum() {
        String starttime = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
        String endtime = DateUtil.format(DateUtil.endOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
        List<Car> byTimestampBetween = carRepository.findByTimestampBetween(starttime, endtime);
        return byTimestampBetween.size();
    }
}
