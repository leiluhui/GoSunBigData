package com.hzgc.cloud.community.dao;

import com.hzgc.cloud.community.model.ImsiAll;

import java.util.List;

public interface ImsiAllMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImsiAll record);

    int insertSelective(ImsiAll record);

    ImsiAll selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImsiAll record);

    int updateByPrimaryKey(ImsiAll record);

    List<ImsiAll> selectByImsi(List<String> imsis);
}