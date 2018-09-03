package com.hzgc.service.address.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class FtpAddressService implements Serializable {
    @Autowired
    @SuppressWarnings("unused")
    FtpRegisterClient register;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Map<String, String> getProperties(String ftpType) {
        Map<String, String> map = new HashMap<>();
        List<FtpRegisterInfo> list = null;
        if ("face".equals(ftpType)){
            list = register.getFaceFtpRegisterInfoList();
        }
        if ("person".equals(ftpType)){
            list = register.getPersonFtpRegisterInfoList();
        }
        if ("car".equals(ftpType)){
            list = register.getCarFtpRegisterInfoList();
        }
        if (list != null && list.size() >0){
            FtpRegisterInfo registerInfo = list.get((int) (Math.random() * list.size()));
            if (registerInfo != null) {
                map.put("ftpIp", registerInfo.getFtpIPAddress());
                map.put("ftpPort", registerInfo.getFtpPort());
                map.put("proxyIP", registerInfo.getProxyIP());
                map.put("proxyPort", registerInfo.getProxyPort());
                map.put("username", registerInfo.getFtpAccountName());
                map.put("password", registerInfo.getFtpPassword());
                map.put("pathRule", registerInfo.getPathRule());
            }
        }
        return map;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    public String getIPAddress(String hostname) {
        if (!StringUtils.isBlank(hostname)){
            return register.getFtpIpMapping().get(hostname);
        }
        return null;
    }
}

