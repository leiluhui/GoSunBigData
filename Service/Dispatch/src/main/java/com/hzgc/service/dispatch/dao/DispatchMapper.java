package com.hzgc.service.dispatch.dao;

import com.hzgc.service.dispatch.model.Dispatch;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface DispatchMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dispatch record);

    int insertSelective(Dispatch record);

    Dispatch selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Dispatch record);

    int updateByPrimaryKeyWithBLOBs(Dispatch record);

    int updateByPrimaryKey(Dispatch record);
}