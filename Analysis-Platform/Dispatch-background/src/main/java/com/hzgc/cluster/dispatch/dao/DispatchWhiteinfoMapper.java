package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchWhiteinfo;

import java.util.List;

public interface DispatchWhiteinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DispatchWhiteinfo record);

    int insertSelective(DispatchWhiteinfo record);

    DispatchWhiteinfo selectByPrimaryKey(Long id);

    List<DispatchWhiteinfo> selectByWhiteId(String whiteId);

    int updateByPrimaryKeySelective(DispatchWhiteinfo record);

    int updateByPrimaryKeyWithBLOBs(DispatchWhiteinfo record);

    int updateByPrimaryKey(DispatchWhiteinfo record);
}