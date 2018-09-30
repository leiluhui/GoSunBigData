package com.hzgc.common.service.api.service;

import com.hzgc.common.service.api.bean.UrlInfo;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.basic.StopWatch;
import com.hzgc.jniface.PictureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class InnerService {
    @Resource(name = "inner")
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    public UrlInfo httpHostNameToIp(String httpHostName) {
        if (httpHostName != null && !"".equals(httpHostName)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            UrlInfo urlInfo = restTemplate.getForObject("http://collect" + BigDataPath.HTTP_HOSTNAME_TO_IP +
                    "?hostNameUrl=" + httpHostName, UrlInfo.class);
            stopWatch.stop();
            log.info("Method httpHostNameToIp, request successfull, total time is:{}", stopWatch.getLastTaskTimeMillis());
            return urlInfo;
        } else {
            log.error("Method httpHostNameToIp, httpUrlList is null or size is 0");
            return new UrlInfo();
        }
    }

    public UrlInfo hostName2Ip(String hostName) {
        if (hostName != null && !"".equals(hostName)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            UrlInfo urlInfo = restTemplate.getForObject("http://collect" + BigDataPath.HOSTNAME_TO_IP +
                    "?hostName=" + hostName, UrlInfo.class);
            stopWatch.stop();
            log.info("Method hostName2Ip, request successfull, total time is:{}", stopWatch.getLastTaskTimeMillis());
            return urlInfo;
        } else {
            log.error("Method hostName2Ip, httpUrlList is null or size is 0");
            return new UrlInfo();
        }
    }

    public Map <String, UrlInfo> hostName2IpBatch(List <String> hostNameList) {
        if (hostNameList != null && hostNameList.size() > 0) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Map <String, UrlInfo> result = restTemplate.postForObject("http://collect" + BigDataPath.HOSTNAME_TO_IP, hostNameList, Map.class);
            stopWatch.stop();
            log.info("Method hostName2IpBatch, request successfull, total time is:{}", stopWatch.getLastTaskTimeMillis());
            return result;
        } else {
            log.error("Method hostName2IpBatch, httpUrlList is null or size is 0");
            return new HashMap <>();
        }
    }

    public PictureData faceFeautreExtract(String base64Str) {
        if (base64Str != null && !"".equals(base64Str)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            ParameterizedTypeReference <PictureData> parameterizedTypeReference =
                    new ParameterizedTypeReference <PictureData>() {
                    };
            ResponseEntity <PictureData> data = restTemplate.exchange("http://collect-ftp" +
                            BigDataPath.FEATURE_EXTRACT_BASE64, HttpMethod.POST,
                    new HttpEntity <>(base64Str), parameterizedTypeReference);
            stopWatch.stop();
            log.info("Method faceFeatureExtract, request seccessull, total time is:{}", stopWatch.getTotalTimeMillis());
            return data.getBody();
        } else {
            log.error("Method faceFeatureExtract, base64Str is null or size is 0");
            return null;
        }
    }
}
