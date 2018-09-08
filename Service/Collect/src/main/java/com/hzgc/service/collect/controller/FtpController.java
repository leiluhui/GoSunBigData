package com.hzgc.service.collect.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.collect.service.FtpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "/ftp", tags = "FTP服务")
public class FtpController {

    @Autowired
    private FtpService ftpService;

    @ApiOperation(value = "根据ftpurl获取图片数据", produces = "image/jpeg")
    @ApiImplicitParam(name = "ftpUrl", value = "ftp路径", dataType = "String", paramType = "query")
    @RequestMapping(value = "ftp_get_image", method = RequestMethod.GET)
    public ResponseEntity <byte[]> getPhotoData(String ftpUrl) {
        if (StringUtils.isBlank(ftpUrl)) {
            log.error("Start get ftp photo, but ftp url is null");
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.info("Start get ftp photo, param is : " + ftpUrl);
        byte[] photo = ftpService.getPhotoByFtpUrl(ftpUrl);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
    }

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    @ApiOperation(value = "获取可绑定ftp地址信息")
    @ApiImplicitParam(name = "ftpType", value = "ftp类型", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.FTP_GET_PROPERTIES, method = RequestMethod.GET)
    public ResponseResult <Map <String, String>> getFtpAddress(@ApiParam(value = "ftp类型") String ftpType) {
        Map <String, String> map = ftpService.getProperties(ftpType);
        return ResponseResult.init(map);
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    @ApiOperation(value = "ftp服务器主机名转IP", response = String.class, responseContainer = "List")
    @ApiImplicitParam(name = "hostname", value = "主机名", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.FTP_GET_IP, method = RequestMethod.GET)
    public ResponseResult <String> getIPAddress(@ApiParam(value = "主机名") String hostname) {
        if (null != hostname) {
            String ip = ftpService.getIPAddress(hostname);
            return ResponseResult.init(ip);
        }
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }

    @ApiOperation(value = "打开抓拍订阅")
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_OPEN, method = RequestMethod.POST)
    public ResponseResult <Boolean> openFtpSubscription(String userId, @RequestBody List <String> ipcIdList) {
        if (StringUtils.isBlank(userId) || ipcIdList.isEmpty()) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean bb = ftpService.openFtpSubscription(userId, ipcIdList);
        return ResponseResult.init(bb);
    }

    @ApiOperation(value = "关闭抓拍订阅")
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_CLOSE, method = RequestMethod.POST)
    public ResponseResult <Boolean> closeFtpSubscription(String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean bb = ftpService.closeFtpSubscription(userId);
        return ResponseResult.init(bb);
    }
}
