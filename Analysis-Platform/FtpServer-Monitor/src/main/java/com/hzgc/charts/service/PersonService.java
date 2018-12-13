package com.hzgc.charts.service;

import com.hzgc.charts.domain.Person;

import java.util.List;

/**
 * created by liang on 2018/12/12
 */
public interface PersonService {

     List<Person> findAll();

     /**
      * 查询所有的car的数量
      */
     long findTotalNum();
}
