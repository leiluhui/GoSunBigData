package com.hzgc.common.service.api.service;

import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.basic.StopWatch;
import com.hzgc.jniface.PictureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class InnerService {
    @Autowired
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    public List<String> httpHostNameToIp(List<String> httpUrlList) {
        if (httpUrlList != null && httpUrlList.size() > 0) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            ParameterizedTypeReference<String[]>
                    parameterizedTypeReference = new ParameterizedTypeReference<String[]>() {};
            ResponseEntity<String[]> arr = restTemplate.exchange("http://collect/http_hostname_to_ip",
                    HttpMethod.POST,
                    new HttpEntity<>(httpUrlList),
                    parameterizedTypeReference);
            stopWatch.stop();
            log.info("Method httpHostNameToIp, request successfull, total time is:{}", stopWatch.getLastTaskTimeMillis());
            return Arrays.asList(arr.getBody());
        } else {
            log.error("Method httpHostNameToIp, httpUrlList is null or size is 0");
            return new ArrayList<>();
        }
    }

    public PictureData faceFeautreExtract(String base64Str) {
        if (base64Str != null && !"".equals(base64Str)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            ParameterizedTypeReference<PictureData> parameterizedTypeReference =
                    new ParameterizedTypeReference<PictureData>() {};
            ResponseEntity<PictureData> data = restTemplate.exchange("http://collect-ftp" +
                            BigDataPath.FEATURE_EXTRACT_BASE64, HttpMethod.POST,
                    new HttpEntity<>(base64Str), parameterizedTypeReference);
            stopWatch.stop();
            log.info("Method faceFeatureExtract, request seccessull, total time is:{}", stopWatch.getTotalTimeMillis());
            return data.getBody();
        } else {
            log.error("Method faceFeatureExtract, base64Str is null or size is 0");
            return null;
        }
    }
}
