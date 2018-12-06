package com.hzgc.cloud.imsi.controller;

import com.hzgc.cloud.imsi.model.MacInfo;
import com.hzgc.cloud.imsi.model.MacParam;
import com.hzgc.cloud.imsi.model.MacVO;
import com.hzgc.cloud.imsi.model.SearchMacDTO;
import com.hzgc.cloud.imsi.service.MacService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
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

    @ApiOperation(value = "MAC信息模糊查询", response = MacVO.class)
    @RequestMapping(value = BigDataPath.SEARCH_MAC, method = RequestMethod.POST)
    public ResponseResult<List<MacVO>> searchIMSI(@RequestBody SearchMacDTO searchMacDTO) {
        if (searchMacDTO == null) {
            log.error("Start search mac info, but searchMacDTO is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (searchMacDTO.getSearchType() != 0 && searchMacDTO.getSearchType() != 1 && searchMacDTO.getSearchType() != 2) {
            log.error("Start search mac info, but searchType is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类型错误，请检查");
        }
        if (searchMacDTO.getStart() < 0) {
            log.error("Start search mac info, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (searchMacDTO.getLimit() <= 0) {
            log.error("Start search mac info, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search mac info, searchMacDTO is:" + JacksonUtil.toJson(searchMacDTO));
        ResponseResult<List<MacVO>> macVOList = macService.searchIMSI(searchMacDTO);
        log.info("Start search mac info successfully");
        return macVOList;
    }
}
