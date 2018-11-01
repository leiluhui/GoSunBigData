package com.hzgc.service.imsi.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
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
    private ImsiService imsiService;

    @RequestMapping(value = BigDataPath.IMSI_SEARCH_BY_TIME, method = RequestMethod.GET)
    public List <ImsiInfo> queryByTime(Long time) {
        if (null == time) {
            log.error("Start search imsi by time, but search option is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start search imsi by time, this time is: " + time);
        List <ImsiInfo> listResponseResult = imsiService.queryByTime(time);
        log.info("Query search imsi is " + JacksonUtil.toJson(listResponseResult));
        return listResponseResult;
    }
}
