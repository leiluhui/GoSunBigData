package com.hzgc.service.clustering.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.util.empty.IsEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClusteringServiceHelper {

    @Autowired
    private FtpRegisterClient registerClient;

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    public String getFtpUrl(String ftpUrl) {

        String hostName = ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":"));
        String ftpServerIP = registerClient.getFtpIpMapping().get(hostName);
        if (IsEmpty.strIsRight(ftpServerIP)) {
            return ftpUrl.replace(hostName, ftpServerIP);
        }
        return ftpUrl;
    }
}
