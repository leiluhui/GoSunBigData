package com.hzgc.service.collect.controller;

import com.hzgc.common.service.api.bean.UrlInfo;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.collect.service.FtpService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "/ftp", tags = "FTP服务")
public class FtpController {

    @Autowired
    private FtpService ftpService;

    @ApiIgnore(value = "内部服务:批量hostname转ip")
    @RequestMapping(value = BigDataPath.HOSTNAME_TO_IP, method = RequestMethod.POST)
    public ResponseEntity <Map <String, UrlInfo>> hostName2IpBatch(@RequestBody List <String> hostNameList) {
        if (hostNameList != null && hostNameList.size() > 0) {
            Map <String, UrlInfo> result = ftpService.hostName2IpBatch(hostNameList);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(result);
        } else {
            log.error("Method:hostName2Ip, hostNameList is null or size is 0");
            return ResponseEntity.badRequest().body(new HashMap <>());
        }
    }

    @ApiIgnore(value = "内部服务:hostname转ip")
    @RequestMapping(value = BigDataPath.HOSTNAME_TO_IP, method = RequestMethod.GET)
    public ResponseEntity <UrlInfo> hostName2Ip(@RequestParam(value = "hostName") String hostName) {
        if (hostName != null && !"".equals(hostName)) {
            UrlInfo urlInfo = ftpService.hostName2Ip(hostName);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(urlInfo);
        } else {
            return ResponseEntity.accepted().build();
        }
    }

    @ApiIgnore(value = "内部服务:带hostname的httpurl转为带ip的url")
    @RequestMapping(value = BigDataPath.HTTP_HOSTNAME_TO_IP, method = RequestMethod.GET)
    public ResponseEntity <UrlInfo> http_hostName2Ip(String hostNameUrl) {
        if (hostNameUrl != null && !"".equals(hostNameUrl)) {
            UrlInfo urlInfo = ftpService.http_hostName2Ip(hostNameUrl);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(urlInfo);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
