package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.FusionImsi;

public interface FusionImsiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FusionImsi record);

    int insertSelective(FusionImsi record);

    FusionImsi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FusionImsi record);

    int updateByPrimaryKey(FusionImsi record);
}