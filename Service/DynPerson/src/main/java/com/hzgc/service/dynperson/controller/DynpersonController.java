package com.hzgc.service.dynperson.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.dynperson.bean.CaptureOption;
import com.hzgc.service.dynperson.bean.CaptureResult;
import com.hzgc.service.dynperson.bean.SingleResults;
import com.hzgc.service.dynperson.service.DynpersonCaptureService;
import com.hzgc.service.dynperson.service.DynpersonHistoryService;
import com.hzgc.service.dynperson.service.DynpersonSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "行人服务")
@Slf4j
@SuppressWarnings("unused")
public class DynpersonController {
    @Autowired
    @SuppressWarnings("unused")
    private DynpersonSearchService dynpersonSearchService;

    @Autowired
    @SuppressWarnings("unused")
    private DynpersonCaptureService dynpersonCaptureService;

    @Autowired
    @SuppressWarnings("unused")
    private DynpersonHistoryService dynpersonHistoryService;


    @ApiOperation(value = "行人抓拍历史查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.CAPTURE_HISTORY, method = RequestMethod.POST)
    public ResponseResult<CaptureResult> getCaptureHistory(
            @RequestBody @ApiParam(value = "行人抓拍查询入参") CaptureOption captureOption){
        if (null == captureOption){
            log.info("CaptrueOption is null,please check");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        dynpersonCaptureService.captureOptionConver(captureOption);
        if (captureOption.getDeviceIpcs() == null ||
                captureOption.getDeviceIpcs().size() <= 0 ||
                captureOption.getDeviceIpcs().get(0) == null) {
            log.error("Start capture history, deviceIpcs option is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start capture history, search option is:" + JSONUtil.toJson(captureOption));

        SingleResults searchResultList =
                dynpersonHistoryService.getCaptureHistory(captureOption);

        CaptureResult captureResult = new CaptureResult();
        captureResult.setSingleResults(searchResultList);
        captureResult.setSearchId(UuidUtil.getUuid());

        return ResponseResult.init(captureResult,searchResultList.getTotal());
    }


}
