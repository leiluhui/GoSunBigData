package com.hzgc.service.collect.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import com.hzgc.service.collect.dao.FtpInfoMapper;
import com.hzgc.service.collect.model.FtpInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FtpPersistence implements CommandLineRunner {
    @Autowired
    private FtpRegisterClient ftpRegisterClient;
    @Autowired
    private FtpInfoMapper ftpInfoMapper;

    @Override
    public void run(String... strings) {
        List <FtpRegisterInfo> ftpRegisterInfoList = ftpRegisterClient.getFtpRegisterInfoList();
        for (FtpRegisterInfo ftpRegisterInfo : ftpRegisterInfoList) {
            String ftpIPAddress = ftpRegisterInfo.getFtpIPAddress();
            //查询数据库，是否存在，存在count + 1,不存在count = 1
            FtpInfo ftpInfo = ftpInfoMapper.selectByFtpAddress(ftpIPAddress);
            if (null != ftpInfo) {
                Integer count = ftpInfo.getCount();
                count++;
                ftpInfo.setCount(count);
                ftpInfoMapper.updateByIpSelective(ftpInfo);
            } else {
                ftpInfo = new FtpInfo();
                ftpInfo.setCount(1);
                ftpInfo.setIp(ftpIPAddress);
                ftpInfoMapper.insertSelective(ftpInfo);
            }
        }
    }
}
