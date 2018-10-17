package com.hzgc.service.dispatch.dao;

import com.hzgc.service.dispatch.model.DispatchRecognize;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface DispatchRecognizeMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchRecognize record);

    int insertSelective(DispatchRecognize record);

    DispatchRecognize selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DispatchRecognize record);

    int updateByPrimaryKey(DispatchRecognize record);
}