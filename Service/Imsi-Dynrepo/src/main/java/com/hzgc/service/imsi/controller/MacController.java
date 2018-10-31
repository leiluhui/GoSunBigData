package com.hzgc.service.imsi.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.model.MacInfo;
import com.hzgc.service.imsi.model.MacParam;
import com.hzgc.service.imsi.service.MacService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class MacController {

    @Autowired
    private MacService macService;

    @RequestMapping(value = BigDataPath.MAC_SEARCH_BY_SNS, method = RequestMethod.POST )
    public ResponseResult<List<MacInfo>> queryBySns(@RequestBody MacParam macParam) {
        if (null == macParam || macParam.getList().size() <= 0) {
            log.error("Start search mac by sns, but search option is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"参数不能为空");
        }
        log.info("Start search mac by sns, this sns is: " + JacksonUtil.toJson(macParam));
        return macService.queryBySns(macParam);
    }
}
