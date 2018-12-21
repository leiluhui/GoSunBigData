package com.hzgc.cloud.community.controller;

import com.hzgc.cloud.community.param.PeopleCaptureCountVO;
import com.hzgc.cloud.community.param.PeopleCaptureDTO;
import com.hzgc.cloud.community.param.PeopleCaptureVO;
import com.hzgc.cloud.community.service.PeopleFocusService;
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
@Api(value = "/community", tags = "聚焦人员服务")
public class PeopleFocusController {
    @Autowired
    private PeopleFocusService peopleFocusService;

    @ApiOperation(value = "聚焦人员人脸、车辆、电围数据查询", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CAPTURE_1MONTH, method = RequestMethod.POST)
    public ResponseResult<List<PeopleCaptureVO>> searchCapture1Month(@RequestBody PeopleCaptureDTO param) {
        if (param == null) {
            log.error("Start search people capture info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (param.getPeopleId() == null) {
            log.error("Start search people capture info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        if (param.getStart() < 0) {
            log.error("Start search people capture info, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (param.getLimit() <= 0) {
            log.error("Start search people capture info, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search people capture info, param is:" + JacksonUtil.toJson(param));
        ResponseResult<List<PeopleCaptureVO>> result = peopleFocusService.searchCapture1Month(param);
        log.info("Search people capture info successfully");
        return result;
    }

    @ApiOperation(value = "人脸、车辆、电围记录数据删除", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.DELETE_RECOGNIZE_RECORD, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deleteRecognizeRecord(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            log.error("Start delete recognize record, but id list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除ID列表为空,请检查！");
        }
        log.info("Start delete recognize record, param:" + JacksonUtil.toJson(idList));
        Integer status = peopleFocusService.deleteRecognizeRecord(idList);
        if (status != 1){
            log.error("Delete recognize record failed");
            return ResponseResult.init(0);
        }
        log.info("Delete recognize record successful");
        return ResponseResult.init(1);
    }


    @ApiOperation(value = "聚焦人员轨迹查询", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_DEVICE_TRACK_1MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureVO>> searchPeopleTrack1Month(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search people capture track, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        log.info("Start search people capture track, people id is:" + peopleId);
        List<PeopleCaptureVO> voList = peopleFocusService.searchPeopleTrack1Month(peopleId);
        log.info("Search people capture track successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "统计聚焦人员每个设备抓拍次数（设备热力图）", response = PeopleCaptureCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_DEVICE_CAPTURE_1MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureCountVO>> countDeviceCaptureNum1Month(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start count people capture number, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        log.info("Start count people capture number, people id is:" + peopleId);
        List<PeopleCaptureCountVO> voList = peopleFocusService.countDeviceCaptureNum1Month(peopleId);
        log.info("Count people capture number successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "统计聚焦人员每天抓拍次数（最近三个月）", response = PeopleCaptureCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CAPTURE_3MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureCountVO>> countCaptureNum3Month(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start count people everyday capture number, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        log.info("Start count people everyday capture number, people id is:" + peopleId);
        List<PeopleCaptureCountVO> voList = peopleFocusService.countCaptureNum3Month(peopleId);
        log.info("Count people everyday capture number successfully");
        return ResponseResult.init(voList);
    }
}
