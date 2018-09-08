package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.House;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseMapper {

    int insert(House record);

    int insertSelective(House record);

    House selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(House record);

    int updateByPrimaryKey(House record);
}