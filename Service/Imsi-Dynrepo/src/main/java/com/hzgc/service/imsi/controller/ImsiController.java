package com.hzgc.service.imsi.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.imsi.model.ImsiVO;
import com.hzgc.service.imsi.model.SearchImsiDTO;
import com.hzgc.service.imsi.service.ImsiProducer;
import com.hzgc.service.imsi.service.ImsiService;
import io.swagger.annotations.ApiOperation;
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
    private ImsiService imsiService;

    @Autowired
    private ImsiProducer imsiProducer;

    @RequestMapping(value = BigDataPath.IMSI_SEARCH_BY_TIME, method = RequestMethod.GET)
    public List <ImsiInfo> queryByTime(Long time) {
        if (null == time) {
            log.error("Start search imsi by time, but search option is null");
            return null;
        }
        log.info("Start search imsi by time, this time is: " + time);
        return imsiService.queryByTime(time);
    }

    @ApiOperation(value = "IMSI信息模糊查询", response = ImsiVO.class)
    @RequestMapping(value = BigDataPath.SEARCH_IMSI, method = RequestMethod.POST)
    public ResponseResult<List<ImsiVO>> searchIMSI(@RequestBody SearchImsiDTO searchImsiDTO) {
        if (searchImsiDTO == null) {
            log.error("Start search imsi info, but searchImsiDTO is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (searchImsiDTO.getSearchType() != 0 && searchImsiDTO.getSearchType() != 1) {
            log.error("Start search imsi info, but searchType is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类型错误，请检查");
        }
        if (searchImsiDTO.getStart() < 0) {
            log.error("Start search imsi info, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (searchImsiDTO.getLimit() <= 0) {
            log.error("Start search imsi info, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search imsi info, searchImsiDTO is:" + JacksonUtil.toJson(searchImsiDTO));
        ResponseResult<List<ImsiVO>> imsiVOList = imsiService.searchIMSI(searchImsiDTO);
        log.info("Start search imsi info successfully");
        return imsiVOList;
    }
}
