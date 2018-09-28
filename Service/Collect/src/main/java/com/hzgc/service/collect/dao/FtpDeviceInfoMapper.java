package com.hzgc.service.collect.dao;

import com.hzgc.service.collect.model.FtpDeviceInfo;

public interface FtpDeviceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FtpDeviceInfo record);

    int insertSelective(FtpDeviceInfo record);

    FtpDeviceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FtpDeviceInfo record);

    int updateByPrimaryKey(FtpDeviceInfo record);
}