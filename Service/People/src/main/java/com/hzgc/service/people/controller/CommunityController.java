package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.people.param.CommunityPeopleCountVO;
import com.hzgc.service.people.param.CommunityPeopleDTO;
import com.hzgc.service.people.param.CommunityPeopleVO;
import com.hzgc.service.people.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation(value = "小区人口数量统计", response = CommunityPeopleCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleCountVO> countCommunityPeople(Long communityId) {
        if (communityId == null){
            log.error("Start count community people, but community id is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"小区ID不能为空，请检查！");
        }
        log.info("Start count community people, param is: " + communityId);
        CommunityPeopleCountVO communityPeopleCountVO = communityService.countCommunityPeople(communityId);
        return ResponseResult.init(communityPeopleCountVO);
    }

    @ApiOperation(value = "小区实有人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE, method = RequestMethod.GET)
    public ResponseResult<List<CommunityPeopleVO>> searchCommunityPeople(CommunityPeopleDTO dto) {
        if (dto == null){
            log.error("Start search community people, but param is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询参数为空，请检查！");
        }
        if (dto.getCommunityId() == null){
            log.error("Start search community people, but communityId is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"小区ID不能为空，请检查！");
        }
        if (dto.getLimit() == 0){
            log.error("Start search community people, but limit is 0 !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"分页行数不能为0，请检查！");
        }
        log.info("Start search community people, param is: "+ JacksonUtil.toJson(dto));
        List<CommunityPeopleVO> communityPeopleVO = communityService.searchCommunityPeople(dto);
        log.info("Search community people successfully!");
        return ResponseResult.init(communityPeopleVO);
    }

    @ApiOperation(value = "小区重点人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_STATUS, method = RequestMethod.GET)
    public ResponseResult<List<CommunityPeopleVO>> searchCommunityImportantPeople(CommunityPeopleDTO param) {
        if (param == null){
            log.error("Start search people ,but param is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null || param.getCommunityId() == 0){
            log.error("Start search people ,but community id is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0){
            log.error("Start search people ,but limit is 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"分页行数不能为0,请检查！");
        }
        log.info("Start search important people,param is :"+ JacksonUtil.toJson(param));
        List<CommunityPeopleVO> voList = communityService.searchCommunityImportantPeople(param);
        log.info("Search community important people successfully!");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区关爱人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CARE, method = RequestMethod.GET)
    public ResponseResult<List<CommunityPeopleVO>> searchCommunityCarePeople(CommunityPeopleDTO param) {
        if (param == null){
            log.error("Start search care people ,but param is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"查询参数为空,请检查！");
        }
        if (param.getCommunityId() == null || param.getCommunityId() == 0){
            log.error("Start search care people ,but community id is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"小区ID不能为空,请检查！");
        }
        if (param.getLimit() == 0){
            log.error("Start search care people ,but limit is 0 ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"分页行数不能为0,请检查！");
        }
        log.info("Start search care people,param is :"+ JacksonUtil.toJson(param));
        List<CommunityPeopleVO> voList = communityService.searchCommunityCarePeople(param);
        log.info("Search community care people successfully!");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区新增人口查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_NEW, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunityNewPeople() {
        return null;
    }

    @ApiOperation(value = "小区迁入人口查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_OUT, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunityOutPeople() {
        return null;
    }

    @ApiOperation(value = "建议迁出查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SUGGEST_OUT, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunitySuggestOut() {
        return null;
    }

    @ApiOperation(value = "确认迁出", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_OUT, method = RequestMethod.DELETE)
    public ResponseResult<String> searchCommunityAffirmOut() {
        return null;
    }

    //@ApiOperation(value = "建议迁入查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SUGGEST_NEW, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunitySuggestNew() {
        return null;
    }

    //@ApiOperation(value = "确认迁入查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW, method = RequestMethod.GET)
    public ResponseResult<String> searchCommunityAffirmNew() {
        return null;
    }
}
