package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.model.ImsiInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface ImsiInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImsiInfo record);

    int insertSelective(ImsiInfo record);

    ImsiInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImsiInfo record);

    int updateByPrimaryKey(ImsiInfo record);

    List <ImsiInfo> selectByTime(ImsiInfo record);
}