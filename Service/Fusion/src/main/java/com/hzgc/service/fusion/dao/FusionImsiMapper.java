package com.hzgc.service.fusion.dao;

import com.hzgc.service.fusion.model.FusionImsi;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FusionImsiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FusionImsi record);

    int insertSelective(FusionImsi record);

    FusionImsi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FusionImsi record);

    int updateByPrimaryKey(FusionImsi record);
}