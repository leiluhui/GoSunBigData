package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Imsi;

public interface ImsiMapper {

    int insert(Imsi record);

    int insertSelective(Imsi record);

    Imsi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Imsi record);

    int updateByPrimaryKey(Imsi record);
}