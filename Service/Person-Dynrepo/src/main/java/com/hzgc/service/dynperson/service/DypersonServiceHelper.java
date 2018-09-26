package com.hzgc.service.dynperson.service;


import com.hzgc.common.service.api.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 动态库实现类
 */
@Component
@Slf4j
public class DypersonServiceHelper {

    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService queryService;

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    String getFtpUrl(String ftpUrl, String ip) {
        return ftpUrl.replace(ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":")), ip);
    }

}

