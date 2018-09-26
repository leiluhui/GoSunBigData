package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.DeviceRecognize;

import java.util.List;

public interface DeviceRecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceRecognize record);

    int insertSelective(DeviceRecognize record);

    DeviceRecognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceRecognize record);

    int updateByPrimaryKey(DeviceRecognize record);

    List<DeviceRecognize> countDeviceCaptureNum1Month(String peopleid);

    List<DeviceRecognize> countCaptureNum3Month(String peopleid);
}