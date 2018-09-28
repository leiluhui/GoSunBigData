package com.hzgc.service.collect.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.RefreshDataCallBack;
import com.hzgc.service.collect.dao.FtpInfoMapper;
import com.hzgc.service.collect.model.FtpInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FtpDiscover extends FtpRegisterClient implements RefreshDataCallBack {

    @Autowired
    private FtpInfoMapper ftpInfoMapper;

    public FtpDiscover(String zkAddress, RefreshDataCallBack callBack) {
        super(zkAddress, callBack);
    }

    @Override
    public void run(PathChildrenCacheEvent event) {
        String ftpPath = event.getData().getPath();
        //数据库的存储
        FtpInfo ftpInfo = ftpInfoMapper.selectByFtpAddress(ftpPath);
        if (null != ftpInfo) {
            Integer count = ftpInfo.getCount();
            count++;
            ftpInfo.setCount(count);
            ftpInfoMapper.updateByIpSelective(ftpInfo);
        } else {
            ftpInfo = new FtpInfo();
            ftpInfo.setCount(1);
            ftpInfo.setIp(ftpPath);
            ftpInfoMapper.insertSelective(ftpInfo);
        }
    }
}
