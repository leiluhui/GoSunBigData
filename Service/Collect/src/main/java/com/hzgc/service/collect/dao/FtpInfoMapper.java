package com.hzgc.service.collect.dao;

import com.hzgc.service.collect.model.FtpInfo;

import java.util.List;

public interface FtpInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FtpInfo record);

    int insertSelective(FtpInfo record);

    FtpInfo selectByPrimaryKey(Integer id);

    FtpInfo selectByFtpAddress(String ip);

    List<FtpInfo> selectByCountAsc();

    int updateByIpSelective(FtpInfo record);

    int updateByPrimaryKey(FtpInfo record);
}