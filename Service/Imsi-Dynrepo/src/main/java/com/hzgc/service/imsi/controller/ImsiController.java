package com.hzgc.service.imsi.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.service.imsi.bean.ImsiBean;
import com.hzgc.service.imsi.bean.ImsiParam;
import com.hzgc.service.imsi.service.ImsiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ImsiController {

    @Autowired
    public ImsiService imsiService;

    @RequestMapping(value = "imsi_info", method = RequestMethod.POST)
    public ResponseResult <List <ImsiBean>> getImsiInfo(@RequestBody ImsiParam imsiParam) {
        if (null == imsiParam) {
            log.error("Start search imsi, but search option is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        imsiService.getImsiInfo(imsiParam);
        return null;
    }
}
