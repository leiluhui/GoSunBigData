package com.hzgc.service.clustering.controller;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.service.bean.PeopleManagerCount;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.export.ClusteringInfo;
import com.hzgc.service.clustering.bean.param.PeopleInParam;
import com.hzgc.service.clustering.service.PeopleInSearchService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "建议迁入人口管理")
public class PeopleInController {
    @Autowired
    private PeopleInSearchService peopleInSearchService;


    @ApiOperation(value = "建议迁入人口信息查询【入参：时间月份，区域】", response = ClusteringInfo.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_SEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringInfo> search(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start search param : " + JSONUtil.toJson(peopleInParam));
        if (peopleInParam != null && peopleInParam.getRegion() != null &&
                peopleInParam.getYearMonth() != null) {
            //若传入的start和limit为空，则默认从第一条开始，取所有
            int start = peopleInParam.getStart() == 0? 1: peopleInParam.getStart();
            int limit = peopleInParam.getLimit() == 0? Integer.MAX_VALUE: peopleInParam.getLimit();

            ClusteringInfo clusteringInfo = peopleInSearchService.searchClustering(peopleInParam.getRegion()
                    , peopleInParam.getYearMonth() , start, limit, peopleInParam.getSortParam());
            return ResponseResult.init(clusteringInfo);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "单个迁入人口详细信息查询【入参：时间月份，区域，抓拍历史记录】", response = Integer.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.PEOPLEIN_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<List<FaceObject>> historyRecordSearch(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start historyRecordSearch param : " + JSONUtil.toJson(peopleInParam));
        if (peopleInParam != null && peopleInParam.getRowKeys() != null &&
                peopleInParam.getRowKeys().size() > 0) {

            int start = peopleInParam.getStart() == 0? 1: peopleInParam.getStart();
            int limit = peopleInParam.getLimit() == 0? Integer.MAX_VALUE: peopleInParam.getLimit();

            List<FaceObject> faceObjectList = peopleInSearchService.historyRecordSearch(
                    peopleInParam.getRowKeys(), start, limit);
            return ResponseResult.init(faceObjectList);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "忽略单个迁入人口【入参：时间月份，区域，聚类ID，忽略聚类】", response = Boolean.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_IGNORE, method = RequestMethod.POST)
    public ResponseResult<Boolean> ignore(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start ignore param : " + JSONUtil.toJson(peopleInParam));
        if(peopleInParam != null && peopleInParam.getYearMonth() != null && peopleInParam.getRegion() != null &&
                peopleInParam.getClusterId() != null && peopleInParam.getFlag() != null){
            boolean succeed = peopleInSearchService.ignoreClustering(peopleInParam.getYearMonth(), peopleInParam.getRegion(),
                    peopleInParam.getClusterId(), peopleInParam.getFlag());
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "删除单个迁入人口【入参：时间月份，区域，聚类ID，忽略聚类】", response = Boolean.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> delete(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start delete param : " + JSONUtil.toJson(peopleInParam));
        if(peopleInParam != null && peopleInParam.getYearMonth() != null && peopleInParam.getRegion() != null &&
                peopleInParam.getClusterId() != null && peopleInParam.getFlag() != null){
            boolean succeed = peopleInSearchService.deleteClustering(peopleInParam.getYearMonth(), peopleInParam.getRegion(),
                    peopleInParam.getClusterId(), peopleInParam.getFlag());
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

//    @ApiOperation(value = "单个人口迁入", response = ResponseResult.class)
//    @RequestMapping(value = BigDataPath.PEOPLEIN_SEARCH, method = RequestMethod.POST)
//    public ResponseResult<Integer> moveIn(
//            @RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoParam objectInfoParam) {
//
//        log.info("[PeopleInController] Start moveIn param : " + JSONUtil.toJson(objectInfoParam));
//        if(objectInfoParam != null) {
//            Integer succeed = 1;
////            Integer succeed = objectInfoHandlerService.addObjectInfo(objectInfoParam);
//            return ResponseResult.init(succeed);
//        } else {
//            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
//        }
//
//    }

    @ApiOperation(value = "统计每个月的建议迁入人口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.PEOPLEIN_TOTLE, method = RequestMethod.GET)
    public List<PeopleManagerCount> getTotleNum(String start_time, String end_time) {
        if(StringUtils.isBlank(start_time) || !start_time.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
            log.error("Start time does not conform to the format: yyyy-MM-dd");
                    return new ArrayList<>();
        }
        if(StringUtils.isBlank(start_time) || !end_time.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
            log.error("End time does not conform to the format: yyyy-MM-dd");
            return new ArrayList<>();
        }
        if(end_time.compareTo(start_time) < 0){
            log.error("End time mast be larger than start time.");
            return new ArrayList<>();
        }
        return peopleInSearchService.getTotleNum(start_time, end_time);
    }

}
