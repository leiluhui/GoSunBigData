package com.hzgc.charts.dao;


import com.hzgc.charts.domain.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * created by liang on 2018/12/12
 */
public interface CarRepository extends ElasticsearchRepository<Car, String> {

     List<Car> findByTimestampBetween(String startTime, String endTime);

}
