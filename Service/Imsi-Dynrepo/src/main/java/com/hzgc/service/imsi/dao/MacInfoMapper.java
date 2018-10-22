package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.model.MacInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace
public interface MacInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MacInfo record);

    int insertSelective(MacInfo record);

    MacInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MacInfo record);

    int updateByPrimaryKey(MacInfo record);
}