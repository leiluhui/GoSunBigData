package com.hzgc.cloud.dyncar.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.cloud.dyncar.bean.CaptureOption;
import com.hzgc.cloud.dyncar.bean.Device;
import com.hzgc.cloud.dyncar.bean.SearchResult;
import com.hzgc.cloud.dyncar.service.CaptureHistoryService;
import com.hzgc.cloud.dyncar.util.DeviceToIpcs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Api(tags = "动态库车辆服务")
@Slf4j
public class DyncarController {

    @Autowired
    private CaptureHistoryService captureHistoryService;

    /**
     * 抓拍历史记录查询
     *
     * @param captureOption 以图搜图入参
     * @return List<SearchResult>
     */
    @ApiOperation(value = "车辆抓拍历史查询", response = SearchResult.class)
    @ApiImplicitParam(name = "searchOption", value = "车辆抓拍历史查询参数", paramType = "body")
    @RequestMapping(value = BigDataPath.DYNCAR_CAPTURE_HISTORY, method = RequestMethod.POST)
    @SuppressWarnings("unused")
    public ResponseResult<SearchResult> getCaptureHistory(
            @RequestBody @ApiParam(value = "车辆属性查询参数") CaptureOption captureOption) {
        if (captureOption == null) {
            log.error("Start query vehicle capture history, capture option is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        if (!(captureOption.getDevices() != null && captureOption.getDevices().size() > 0)) {
            log.info("Device id is null,please set device id");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"当前小区没有订阅摄相机");
        }
        Map <String, Device> ipcMapping = DeviceToIpcs.getIpcMapping(captureOption.getDevices());
        captureOption.setIpcMapping(ipcMapping);
        log.info("Start query vehicle capture history, search option is:" + JacksonUtil.toJson(captureOption));
        SearchResult searchResult = captureHistoryService.getCaptureHistory(captureOption);
        return ResponseResult.init(searchResult);
    }
}
