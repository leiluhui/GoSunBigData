package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.DeviceRecognize;

public interface DeviceRecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceRecognize record);

    int insertSelective(DeviceRecognize record);

    DeviceRecognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceRecognize record);

    int updateByPrimaryKey(DeviceRecognize record);
}