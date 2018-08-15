package com.hzgc.service.clustering.controller;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.service.bean.PeopleManagerCount;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.export.ClusteringInfo;
import com.hzgc.service.clustering.bean.param.PeopleInParam;
import com.hzgc.service.clustering.bean.param.ResidentParam;
import com.hzgc.service.clustering.service.ClusteringSearchService;
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

    @Autowired
    private ClusteringSearchService clusteringSearchService;


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

    @ApiOperation(value = "单个人口迁入", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_MOVEIN, method = RequestMethod.PUT)
    public ResponseResult<Boolean> moveIn(
            @RequestBody @ApiParam(value = "对象信息封装类") ResidentParam param,
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start moveIn ResidentParam : " + JSONUtil.toJson(param));
        log.info("[PeopleInController] Start moveIn peoleInParam : " + JSONUtil.toJson(peopleInParam));
        if (param == null) {
            log.error("[PeopleInController] Start moveIn add person, but param is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象人口信息为空，请检查！");
        }
        if (StringUtils.isBlank(param.getRegionID())) {
            log.error("[PeopleInController] Start moveIn add person, but the region is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象区域ID为空，请检查！");
        }
        if (param.getPictureDatas() == null || param.getPictureDatas().getImageData() == null) {
            log.error("[PeopleInController] Start moveIn add person, but the picture data is empty!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象照片数据为空，请检查！");
        }
        boolean authentication_idCode = clusteringSearchService.authentication_idCode(param);
        if (!authentication_idCode) {
            log.error("[PeopleInController] Start moveIn add person, but the idcard format is error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证格式错误，请检查！");
        }
        boolean isExists_idCode = clusteringSearchService.isExists_idCode(param);
        if (isExists_idCode) {
            log.error("[PeopleInController] Start moveIn add person, but the idcard already exists!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证已存在，请检查！");
        }
        Integer succeed = clusteringSearchService.addPerson(param);

        if(succeed == 0) {
            if(peopleInParam != null && peopleInParam.getYearMonth() != null && peopleInParam.getRegion() != null &&
                    peopleInParam.getClusterId() != null && peopleInParam.getFlag() != null){
                boolean result = peopleInSearchService.deleteClustering(peopleInParam.getYearMonth(), peopleInParam.getRegion(),
                        peopleInParam.getClusterId(), peopleInParam.getFlag());
                return ResponseResult.init(result);
            } else {
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
            }
        }
        else {
            return ResponseResult.error(succeed, "迁入对象失败！");
        }
    }

    @ApiOperation(value = "统计每个月的建议迁入人口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.PEOPLEIN_TOTLE, method = RequestMethod.GET)
    public List<PeopleManagerCount> getTotleNum(String start_time, String end_time) {
        if(StringUtils.isBlank(start_time) || !start_time.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
            log.error("[PeopleInController] getTotleNum Start time does not conform to the format: yyyy-MM-dd");
            return new ArrayList<>();
        }
        if(StringUtils.isBlank(start_time) || !end_time.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
            log.error("[PeopleInController] getTotleNum End time does not conform to the format: yyyy-MM-dd");
            return new ArrayList<>();
        }
        if(end_time.compareTo(start_time) < 0){
            log.error("[PeopleInController] getTotleNum End time mast be larger than start time.");
            return new ArrayList<>();
        }
        return peopleInSearchService.getTotleNum(start_time, end_time);
    }

}
