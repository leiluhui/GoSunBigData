package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Imsi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImsiMapper {

    int insert(Imsi record);

    int insertSelective(Imsi record);

    Imsi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Imsi record);

    int updateByPrimaryKey(Imsi record);

    List<Imsi> selectImsiIdsByImsi(String imsi);
}