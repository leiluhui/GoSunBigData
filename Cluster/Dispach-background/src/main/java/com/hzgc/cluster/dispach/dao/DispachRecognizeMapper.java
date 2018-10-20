package com.hzgc.cluster.dispach.dao;

import com.hzgc.cluster.dispach.model.DispachRecognize;

public interface DispachRecognizeMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispachRecognize record);

    int insertSelective(DispachRecognize record);

    DispachRecognize selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DispachRecognize record);

    int updateByPrimaryKey(DispachRecognize record);
}