package com.hzgc.service.imsi.controller;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.imsi.model.ImsiInfo;
import com.hzgc.service.imsi.service.ImsiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ImsiController {

    @Autowired
    public ImsiService imsiService;

    @Autowired
    private PlatformService platformService;

    @RequestMapping(value = BigDataPath.IMSI_SEARCH_BY_TIME, method = RequestMethod.GET)
    public ResponseResult<List <ImsiInfo>> queryByTime(Long time) {
        if (null == time) {
            log.error("Start search imsi by time, but search option is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start search imsi by time, this time is: " + time);
        return imsiService.queryByTime(time);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String queryByTime() {
        Map <String, CameraQueryDTO> cameraInfoByBatchIpc = platformService.getCameraInfoByBatchIpc(new ArrayList <>());
        return JSON.toJSONString(cameraInfoByBatchIpc);
    }

}
