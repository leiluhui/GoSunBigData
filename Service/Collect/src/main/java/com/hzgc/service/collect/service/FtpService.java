package com.hzgc.service.collect.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import com.hzgc.common.collect.facesub.FtpSubscribeClient;
import com.hzgc.service.collect.util.FtpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FtpService implements Serializable {
    @Autowired
    private FtpRegisterClient register;

    @Autowired
    private FtpSubscribeClient subscribe;

    public byte[] getPhotoByFtpUrl(String ftpUrl) {
        // FTP匿名账号Anonymous和密码
        return FtpUtils.downloadftpFile2Bytes(ftpUrl,"anonymous",null);
    }

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

    public boolean openFtpSubscription(String sessionId, List<String> ipcIdList) {
        if (!sessionId.equals("") && !ipcIdList.isEmpty()) {
            subscribe.updateSessionPath(sessionId, ipcIdList);
            return true;
        }
        return false;
    }

    public boolean closeFtpSubscription(String sessionId) {
        if (!sessionId.equals("")) {
            subscribe.deleteSessionPath(sessionId);
            return true;
        }
        return false;
    }
}

