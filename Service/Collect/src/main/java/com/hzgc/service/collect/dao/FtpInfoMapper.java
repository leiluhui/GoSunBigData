package com.hzgc.service.collect.dao;

import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import com.hzgc.service.collect.model.FtpInfo;

public interface FtpInfoMapper {
    int insert(FtpInfo record);

    int insertSelective(FtpInfo record);

    Integer updateSelective(FtpInfo info);

    FtpInfo selectSelective(FtpRegisterInfo registerInfo);

    FtpInfo searchsame(FtpRegisterInfo registerInfo);
}