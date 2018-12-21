package com.hzgc.cloud.community.controller;

import com.hzgc.cloud.community.param.ImportantPeopleRecognizeHistoryVO;
import com.hzgc.cloud.community.param.ImportantRecognizeDTO;
import com.hzgc.cloud.community.param.ImportantRecognizeVO;
import com.hzgc.cloud.community.service.PeopleRecognizeService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Api(value = "/community", tags = "人员识别记录服务")
public class PeopleRecognizeController {
    @Autowired
    private PeopleRecognizeService peopleRecognizeService;

    @ApiOperation(value = "重点人员识别记录查询", response = ImportantRecognizeVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_IMPORTANT_PEOPLE_RECOGNIZE, method = RequestMethod.POST)
    public ResponseResult<ImportantRecognizeVO> importantPeopleRecognize(
            @RequestBody ImportantRecognizeDTO param) {
        if (param == null) {
            log.error("Start search important people recognize, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (param.getRegionId() == null) {
            log.error("Start search important people recognize, but region id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询区域不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getStartTime())) {
            log.error("Start search important people recognize, but start time is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始时间不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getEndTime())) {
            log.error("Start search important people recognize, but end time is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "结束时间不能为空,请检查！");
        }
        if (param.getStart() < 0) {
            log.error("Start search important people recognize, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (param.getLimit() <= 0) {
            log.error("Start search important people recognize, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search important people recognize, param is:" + JacksonUtil.toJson(param));
        ImportantRecognizeVO vo = peopleRecognizeService.importantPeopleRecognize(param);
        log.info("Search important people recognize successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "重点人员告警展示(大数据可视化首页左下角)", response = ImportantPeopleRecognizeHistoryVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_IMPORTANT_PEOPLE_RECOGNIZE_HISTORY, method = RequestMethod.GET)
    public ResponseResult<List<ImportantPeopleRecognizeHistoryVO>> importantPeopleRecognizeHistory(Long regionId) {
        if (regionId == null) {
            log.error("Start search important people recognize history, but region id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询区域不能为空,请检查！");
        }
        log.info("Start search important people recognize history, region id is:" + regionId);
        List<ImportantPeopleRecognizeHistoryVO> voList = peopleRecognizeService.importantPeopleRecognizeHistory(regionId);
        log.info("Search important people recognize successfully");
        return ResponseResult.init(voList);
    }
}
