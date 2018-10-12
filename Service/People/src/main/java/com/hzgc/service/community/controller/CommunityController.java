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
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchCommunityPeople(@RequestBody PeopleDTO param) {
        if (param == null) {
            log.error("Start search community people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community people, but communityId is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空，请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0，请检查！");
        }
        log.info("Start search community people, param is:" + JacksonUtil.toJson(param));
        List<PeopleVO> peopleVO = communityService.searchCommunityPeople(param);
        log.info("Search community people successfully");
        return ResponseResult.init(peopleVO);
    }

    @ApiOperation(value = "小区重点人口查询", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_IMPORTANT, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchCommunityImportantPeople(@RequestBody PeopleDTO param) {
        if (param == null) {
            log.error("Start search community important people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
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
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CARE, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchCommunityCarePeople(@RequestBody PeopleDTO param) {
        if (param == null) {
            log.error("Start search community care people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
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

    @ApiOperation(value = "小区新增人口查询（上月确认迁入数量）", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_NEW, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchCommunityNewPeople(@RequestBody PeopleDTO param) {
        System.out.println(JacksonUtil.toJson(param));
        if (param == null) {
            log.error("Start search community new people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
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

    @ApiOperation(value = "小区迁出人口查询（上月确认迁出数量）", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_OUT, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchCommunityOutPeople(@RequestBody PeopleDTO param) {
        if (param == null) {
            log.error("Start search community out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
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

    @ApiOperation(value = "小区迁入迁出人口统计（实有人口首页）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT_NEW_OUT, method = RequestMethod.POST)
    public ResponseResult<List<NewAndOutPeopleCounVO>> countCommunityNewAndOutPeople(@RequestBody NewAndOutPeopleCountDTO param) {
        if (param == null) {
            log.error("Start count community new and out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start count community new and out people, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        if (param.getCommunityIdList() == null || param.getCommunityIdList().size() == 0) {
            log.error("Start count community new and out people, but community id list is null");
            return ResponseResult.init(null);
        }
        if (param.getMonth() == null || param.getMonth().length() != 6) {
            log.error("Start count community new and out people, but month error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份有误,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start count community new and out people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        log.info("Start count community new and out people, param is :" + JacksonUtil.toJson(param));
        int totalNum = param.getCommunityIdList().size();
        List<NewAndOutPeopleCounVO> voList = communityService.countCommunityNewAndOutPeople(param);
        log.info("Count community new and out people successfully!");
        return ResponseResult.init(voList, totalNum);
    }

    @ApiOperation(value = "小区迁入迁出人口查询（实有人口展示）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_OUT, method = RequestMethod.POST)
    public ResponseResult<NewAndOutPeopleSearchVO> searchCommunityNewAndOutPeople(@RequestBody NewAndOutPeopleSearchDTO param) {
        if (param == null) {
            log.error("Start search community new and out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community new and out people, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start search community new and out people, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        if (param.getMonth() == null || param.getMonth().length() != 6) {
            log.error("Start search community new and out people, but month error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份有误,请检查！");
        }
        if (param.getLimit() == 0) {
            log.error("Start search community new and out people, but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能为0,请检查！");
        }
        if (param.getType() != 0 && param.getType() != 1) {
            log.error("Start search community new and out people, but type error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类型有误,请检查！");
        }
        if (param.getTypeStatus() != 0 && param.getTypeStatus() != 1 && param.getTypeStatus() != 2) {
            log.error("Start search community new and out people, but type status error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类别状态有误,请检查！");
        }
        log.info("Start search community new and out people, param is :" + JacksonUtil.toJson(param));
        NewAndOutPeopleSearchVO vo = communityService.searchCommunityNewAndOutPeople(param);
        log.info("Search community new and out people successfully!");
        return ResponseResult.init(vo, vo != null ? vo.getTotalNum() : 0);
    }

    @ApiOperation(value = "小区迁入迁出人口信息查询", response = CommunityPeopleInfoVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_INFO, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleInfoVO> searchCommunityPeopleInfo(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search community people info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        log.info("Start search community people info, people id is:" + peopleId);
        CommunityPeopleInfoVO vo = communityService.searchCommunityPeopleInfo(peopleId);
        log.info("Search community people info successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "身份证精准查询", response = CommunityPeopleInfoVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_INFO_IDCARD, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleInfoVO> searchPeopleByIdCard(String idCard) {
        if (idCard == null) {
            log.info("Start search people by idCard, but idCard is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询身份证不能为空,请检查!");
        }
        log.info("Start search people by idCard, idCard is:" + idCard);
        CommunityPeopleInfoVO people = communityService.searchPeopleByIdCard(idCard);
        log.info("Search people by idCard successfully, result:" + JacksonUtil.toJson(people));
        return ResponseResult.init(people);
    }

    @ApiOperation(value = "小区迁入人口抓拍详情", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_CAPTURE, method = RequestMethod.POST)
    public ResponseResult<CaptureDetailsVO> searchCommunityNewPeopleCaptureDetails(@RequestBody CaptureDetailsDTO param) {
        if (param.getPeopleId() == null) {
            log.error("Start search community new people capture details, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community new people capture details, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start search community new people capture details, but mouth is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        log.info("Start search community new people capture details, param is:" + JacksonUtil.toJson(param));
        CaptureDetailsVO vo = communityService.searchCommunityNewPeopleCaptureDetails(param);
        log.info("Search community new people capture details successfully");
        return ResponseResult.init(vo);
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
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_OUT, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmOut(@RequestBody AffirmOperationDTO param) {
        if (param == null) {
            log.error("Start affirm out operation, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getPeopleId())) {
            log.error("Start affirm out operation, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start affirm out operation, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start affirm out operation, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "月份不能为空,请检查！");
        }
        if (param.getIsconfirm() != 2 && param.getIsconfirm() != 3) {
            log.error("Start affirm out operation, but isconfirm is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "确认标签有误,请检查！");
        }
        log.info("Start affirm out operation, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmOut(param);
        log.info("Affirm out operation successfully");
        return ResponseResult.init(integer);
    }

    @ApiOperation(value = "小区确认迁入操作", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmNew(@RequestBody AffirmOperationDTO param) {
        if (param == null) {
            log.error("Start affirm new operation, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getPeopleId())) {
            log.error("Start affirm new operation, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start affirm new operation, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start affirm new operation, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "月份不能为空,请检查！");
        }
        if (param.getIsconfirm() != 2 && param.getIsconfirm() != 3) {
            log.error("Start affirm new operation, but isconfirm is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "确认标签有误,请检查！");
        }
        if (param.getFlag() != 0 && param.getFlag() != 1) {
            log.error("Start affirm new operation, but flag is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "迁入状态有误,请检查！");
        }
        log.info("Start affirm new operation, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmNew(param);
        log.info("Affirm new operation successfully");
        return ResponseResult.init(integer);
    }

    @ApiOperation(value = "小区迁入新增人员操作", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW_HANDLE, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmNew_newPeopleHandle(@RequestBody NewPeopleHandleDTO param) {
        if (param == null) {
            log.error("Start new people handle, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getNewPeopleId())) {
            log.error("Start new people handle, but new people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "新增人员ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getSearchPeopleId())) {
            log.error("Start new people handle, but search people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start new people handle, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "社区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getCapturePicture())) {
            log.error("Start new people handle, but capture picture is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "新增人员原图不能为空,请检查！");
        }
        log.info("Start new people handle, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmNew_newPeopleHandle(param);
        if (integer == 1) {
            log.info("New people handle successfully");
        }
        return ResponseResult.init(integer);
    }

    @ApiOperation(value = "聚焦人员抓拍、电围数据查询", response = PeopleCaptureVO.class)
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
    public ResponseResult<List<PeopleCaptureVO>> searchPeopleTrack1Month(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search people capture track, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "聚焦人员ID不能为空,请检查！");
        }
        log.info("Start search people capture track, people id is:" + peopleId);
        List<PeopleCaptureVO> voList = communityService.searchPeopleTrack1Month(peopleId);
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
        List<PeopleCaptureCountVO> voList = communityService.countDeviceCaptureNum1Month(peopleId);
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
        List<PeopleCaptureCountVO> voList = communityService.countCaptureNum3Month(peopleId);
        log.info("Count people everyday capture number successfully");
        return ResponseResult.init(voList);
    }
}
