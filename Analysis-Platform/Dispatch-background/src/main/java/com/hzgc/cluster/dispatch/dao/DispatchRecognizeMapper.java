package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchRecognize;

public interface DispatchRecognizeMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchRecognize record);

    int insertSelective(DispatchRecognize record);

    DispatchRecognize selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DispatchRecognize record);

    int updateByPrimaryKey(DispatchRecognize record);
}