package com.hzgc.service.imsi.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.service.imsi.model.ImsiInfo;
import com.hzgc.service.imsi.service.ImsiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ImsiController {

    @Autowired
    public ImsiService imsiService;

    @RequestMapping(value = "query_by_time", method = RequestMethod.GET)
    public ResponseResult<List <ImsiInfo>> queryByTime(Long time) {
        if (null == time) {
            log.error("Start search imsi by time, but search option is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start search imsi by time, this time is: " + time);
        return imsiService.queryByTime(time);
    }
}
