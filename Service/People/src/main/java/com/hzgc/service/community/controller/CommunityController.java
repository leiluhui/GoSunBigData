package com.hzgc.service.community.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.community.param.*;
import com.hzgc.service.community.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Api(value = "/community", tags = "社区人口库服务")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @ApiOperation(value = "小区人口数量统计", response = PeopleCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT, method = RequestMethod.GET)
    public ResponseResult<PeopleCountVO> countCommunityPeople(Long communityId) {
        if (communityId == null) {
            log.error("Start count community people info, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空，请检查！");
        }
        log.info("Start count community people info, community id is:" + communityId);
        PeopleCountVO peopleCountVO = communityService.countCommunityPeople(communityId);
        log.info("Count community people info successfully");
        return ResponseResult.init(peopleCountVO);
    }

    @ApiOperation(value = "小区实有人口查询", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE, method = RequestMethod.GET)
    public ResponseResult<List<PeopleVO>> searchCommunityPeople(PeopleDTO dto) {
        if (dto == null) {
            log.error("Start search community people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查！");
        }
        if (dto.getCommunityId() == null) {
            log.error("Start search community people, but communityId is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空，请检查！");
        }
        if (dto.getLimit() == 0) {
            log.error("Start search community people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0，请检查！");
        }
        log.info("Start search community people, param is:" + JacksonUtil.toJson(dto));
        List<PeopleVO> peopleVO = communityService.searchCommunityPeople(dto);
        log.info("Search community people successfully");
        return ResponseResult.init(peopleVO);
    }

    @ApiOperation(value = "小区重点人口查询", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_IMPORTANT, method = RequestMethod.GET)
    public ResponseResult<List<PeopleVO>> searchCommunityImportantPeople(PeopleDTO param) {
        if (param == null) {
            log.error("Start search community important people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null || param.getCommunityId() == 0) {
            log.error("Start search community important people, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community important people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search community important people, param is:" + JacksonUtil.toJson(param));
        List<PeopleVO> voList = communityService.searchCommunityImportantPeople(param);
        log.info("Search community important people successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区关爱人口查询", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CARE, method = RequestMethod.GET)
    public ResponseResult<List<PeopleVO>> searchCommunityCarePeople(PeopleDTO param) {
        if (param == null) {
            log.error("Start search community care people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null || param.getCommunityId() == 0) {
            log.error("Start search community care people, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community care people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search community care people, param is:" + JacksonUtil.toJson(param));
        List<PeopleVO> voList = communityService.searchCommunityCarePeople(param);
        log.info("Search community care people successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区新增人口查询（上月确认迁入数量）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_NEW, method = RequestMethod.GET)
    public ResponseResult<List<PeopleVO>> searchCommunityNewPeople(PeopleDTO param) {
        if (param == null) {
            log.error("Start search community new people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community new people, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community new people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search community new people, param is:" + JacksonUtil.toJson(param));
        List<PeopleVO> voList = communityService.searchCommunityNewPeople(param);
        log.info("Search community new people successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区迁出人口查询（上月确认迁出数量）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_OUT, method = RequestMethod.GET)
    public ResponseResult<List<PeopleVO>> searchCommunityOutPeople(PeopleDTO param) {
        if (param == null) {
            log.error("Start search community out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community out people, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community out people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search community out people, param is:" + JacksonUtil.toJson(param));
        List<PeopleVO> voList = communityService.searchCommunityOutPeople(param);
        log.info("Search community out people successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区迁入迁出人口统计（疑似与确认）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT_NEW_OUT, method = RequestMethod.GET)
    public ResponseResult<List<NewAndOutPeopleCounVO>> countCommunityNewAndOutPeople(NewAndOutPeopleCountDTO param) {
        if (param == null){
            log.error("Start count community new and out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询参数为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())){
            log.error("Start count community new and out people, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询月份为空,请检查！");
        }
        if (param.getCommunityIdList() == null || param.getCommunityIdList().size() == 0){
            log.error("Start count community new and out people, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询小区ID列表为空,请检查！");
        }
        if (param.getMonth() == null || param.getMonth().length() != 6){
            log.error("Start count community new and out people, but month error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询月份有误,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start count community new and out people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start count community new and out people, param is :"+ JacksonUtil.toJson(param));
        List<NewAndOutPeopleCounVO> voList = communityService.countCommunityNewAndOutPeople(param);
        log.info("Count community new and out people successfully!");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区迁入迁出人口查询（疑似与确认）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_OUT, method = RequestMethod.GET)
    public ResponseResult<NewAndOutPeopleSearchVO> searchCommunitySuggestOut(NewAndOutPeopleSearchDTO param) {
        return null;
    }

    @ApiOperation(value = "小区迁入人口抓拍详情", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_CAPTURE, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunityNewPeopleCapture(String peopleId) {
        return null;
    }

    @ApiOperation(value = "小区迁出人口最后抓拍查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_OUT_LAST_CAPTURE, method = RequestMethod.GET)
    public ResponseResult<OutPeopleLastCaptureVO> searchCommunityOutPeopleLastCapture(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search community people last capture info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        log.info("Start search community people last capture info, people id is:" + peopleId);
        OutPeopleLastCaptureVO vo = communityService.searchCommunityOutPeopleLastCapture(peopleId);
        log.info("Search community people last capture info successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "小区确认迁出操作", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_OUT, method = RequestMethod.DELETE)
    public ResponseResult<String> searchCommunityAffirmOut() {
        return null;
    }

    @ApiOperation(value = "小区确认迁入操作", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunityAffirmNew() {
        return null;
    }

    @ApiOperation(value = "聚焦人员抓拍、电围数据查询", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CAPTURE_1MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureVO>> searchCapture1Month(PeopleCaptureDTO param) {
        if (param == null) {
            log.error("Start search people capture info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getPeopleId() == null) {
            log.error("Start search people capture info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search people capture info, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search people capture info, param is:" + JacksonUtil.toJson(param));
        List<PeopleCaptureVO> voList = communityService.searchCapture1Month(param);
        log.info("Search people capture info successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "聚焦人员轨迹查询", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_DEVICE_TRACK_1MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureVO>> searchPeopleTrack1Month(PeopleCaptureDTO param) {
        if (param == null) {
            log.error("Start search people capture track, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getPeopleId() == null) {
            log.error("Start search people capture track, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search people capture track, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start search people capture track, param is:" + JacksonUtil.toJson(param));
        List<PeopleCaptureVO> voList = communityService.searchPeopleTrack1Month(param);
        log.info("Search people capture track successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "统计聚焦人员每个设备抓拍次数", response = PeopleCaptureCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_DEVICE_CAPTURE_1MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureCountVO>> countDeviceCaptureNum1Month(PeopleCaptureDTO param) {
        if (param == null) {
            log.error("Start count people capture number, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getPeopleId() == null) {
            log.error("Start count people capture number, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start count people capture number, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start count people capture number, param is:" + JacksonUtil.toJson(param));
        List<PeopleCaptureCountVO> voList = communityService.countDeviceCaptureNum1Month(param);
        log.info("Count people capture number successfully");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "统计聚焦人员每天抓拍次数", response = PeopleCaptureCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CAPTURE_3MONTH, method = RequestMethod.GET)
    public ResponseResult<List<PeopleCaptureCountVO>> countCaptureNum3Month(PeopleCaptureDTO param) {
        if (param == null) {
            log.error("Start count people everyday capture number, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        if (param.getPeopleId() == null) {
            log.error("Start count people everyday capture number, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start count people everyday capture number, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start count people everyday capture number, param is:" + JacksonUtil.toJson(param));
        List<PeopleCaptureCountVO> voList = communityService.countCaptureNum3Month(param);
        log.info("Count people everyday capture number successfully");
        return ResponseResult.init(voList);
    }
}
