package com.hzgc.service.people.controller;

import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.people.param.CommunityPeopleCountVO;
import com.hzgc.service.people.param.CommunityPeopleVO;
import com.hzgc.service.people.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/community", tags = "社区人口库服务")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @ApiOperation(value = "小区人口数量统计", response = CommunityPeopleCountVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleCountVO> countCommunityPeople(Long communityId) {
        return null;
    }

    @ApiOperation(value = "小区实有人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleVO> searchCommunityPeople(Long communityId) {
        return null;
    }

    @ApiOperation(value = "小区重点人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_STATUS, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleVO> searchCommunityStatusPeople(Long communityId) {
        return null;
    }

    @ApiOperation(value = "小区关爱人口查询", response = CommunityPeopleVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_CARE, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleVO> searchCommunityCarePeople(Long communityId) {
        return null;
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
