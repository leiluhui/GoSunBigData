package com.hzgc.service.clustering.controller;

import com.hzgc.common.faceclustering.PeopleInAttribute;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.export.Locus;
import com.hzgc.service.clustering.bean.export.PeopleInHistoryRecord;
import com.hzgc.service.clustering.bean.export.PeopleInResult;
import com.hzgc.service.clustering.bean.param.PeopleInParam;
import com.hzgc.service.clustering.bean.param.ResidentParam;
import com.hzgc.service.clustering.service.ClusteringSearchService;
import com.hzgc.service.clustering.service.PeopleInSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "建议迁入人口管理")
public class PeopleInController {
    @Autowired
    private PeopleInSearchService peopleInSearchService;

    @Autowired
    private ClusteringSearchService clusteringSearchService;


    @ApiOperation(value = "建议迁入人口信息查询【入参：时间月份，区域】", response = PeopleInResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_SEARCH, method = RequestMethod.POST)
    public ResponseResult<PeopleInResult> search(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start search param : " + JSONUtil.toJson(peopleInParam));
        if (peopleInParam != null && peopleInParam.getRegion() != null &&
                peopleInParam.getYearMonth() != null) {
            //若传入的start和limit为空，则默认从第一条开始，取所有
            int start = peopleInParam.getStart() <= 0? 0: peopleInParam.getStart();
            int limit = peopleInParam.getLimit() == 0? Integer.MAX_VALUE: peopleInParam.getLimit();

            PeopleInResult peopleInResult = peopleInSearchService.searchAllClustering(peopleInParam.getRegion()
                    , peopleInParam.getYearMonth().replaceAll("[^\\d]+",""), start, limit, peopleInParam.getSortParam());
            log.info("[PeopleInController] search result : " + peopleInResult);
            return ResponseResult.init(peopleInResult);
        } else {
            log.info("[PeopleInController] search param Error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "单个迁入人口详细信息查询【入参：时间月份，区域，聚类ID，忽略聚类】", response = Integer.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.PEOPLEIN_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<List<PeopleInHistoryRecord>> historyRecordSearch(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start historyRecordSearch param : " + JSONUtil.toJson(peopleInParam));
        if(peopleInParam != null && peopleInParam.getYearMonth() != null && peopleInParam.getRegion() != null &&
                peopleInParam.getClusterId() != null && peopleInParam.getFlag() != null) {
            PeopleInAttribute peopleInAttribute = peopleInSearchService.searchClustering(peopleInParam.getYearMonth().replaceAll("[^\\d]+", ""), peopleInParam.getRegion(), peopleInParam.getClusterId(), peopleInParam.getFlag());
            if (peopleInAttribute != null) {
                int start = peopleInParam.getStart() == 0 ? 0 : peopleInParam.getStart();
                int limit = peopleInParam.getLimit() == 0 ? Integer.MAX_VALUE : peopleInParam.getLimit();
                List<PeopleInHistoryRecord> peopleInHistoryRecordList = peopleInSearchService.historyRecordSearch(peopleInAttribute.getRowKeys(), start, limit);
                return ResponseResult.init(peopleInHistoryRecordList);
            } else {
                log.info("[PeopleInController] historyRecordSearch Not Data!");
                return ResponseResult.error(RestErrorCode.UNKNOWN);
            }
        } else {
            log.info("[PeopleInController] historyRecordSearch param Error!");
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
            boolean succeed = peopleInSearchService.ignoreClustering(peopleInParam.getYearMonth().replaceAll("[^\\d]+",""), peopleInParam.getRegion(),
                    peopleInParam.getClusterId(), peopleInParam.getFlag());
            log.info("[PeopleInController] ignore result : " + succeed);
            return ResponseResult.init(succeed);
        } else {
            log.info("[PeopleInController] ignore param Error!");
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
            boolean succeed = peopleInSearchService.deleteClustering(peopleInParam.getYearMonth().replaceAll("[^\\d]+",""), peopleInParam.getRegion(),
                    peopleInParam.getClusterId(), peopleInParam.getFlag());
            log.info("[PeopleInController] delete result : " + succeed);
            return ResponseResult.init(succeed);
        } else {
            log.info("[PeopleInController] delete param Error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "单个人口迁入【入参:对象信息封装类】", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEIN_MOVEIN, method = RequestMethod.POST)
    public ResponseResult<Integer> moveIn(
            @RequestBody @ApiParam(value = "对象信息封装类") ResidentParam param) {

        log.info("[PeopleInController] Start moveIn ResidentParam : " + JSONUtil.toJson(param));
        if (param == null) {
            log.error("[PeopleInController] Start moveIn add person,but param is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象人口信息为空，请检查！");
        }
        if (StringUtils.isBlank(param.getRegionID())) {
            log.error("[PeopleInController] Start moveIn add person, but the region is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象区域ID为空，请检查！");
        }
        if (param.getPictureDatas() == null || param.getPictureDatas().getImageData() == null) {
            log.error("[PeopleInController] Start moveIn add person,but the picture data is empty!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象照片数据为空，请检查！");
        }
        boolean authentication_idCode = clusteringSearchService.authentication_idCode(param);
        if (!authentication_idCode) {
            log.error("[PeopleInController] Start moveIn add person,but the idcard format is error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证格式错误，请检查！");
        }
        boolean isExists_idCode = clusteringSearchService.isExists_idCode(param);
        if (isExists_idCode) {
            log.error("[PeopleInController] Start moveIn add person, but the idcard already exists!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证已存在，请检查！");
        }
        Integer succeed = clusteringSearchService.addPerson(param);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            log.error("[PeopleInController] Start moveIn add person, but add failed!");
            return ResponseResult.error(succeed, "添加对象失败！");
        }
    }

    @ApiOperation(value = "单个人口轨迹【入参：时间月份，区域，聚类ID，忽略聚类】", response = Locus.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.PEOPLEIN_LOCUS, method = RequestMethod.POST)
    public ResponseResult<List<Locus>> locus(
            @RequestBody @ApiParam(value = "建议迁入人口入参") PeopleInParam peopleInParam) {

        log.info("[PeopleInController] Start capturelocus param : " + JSONUtil.toJson(peopleInParam));
        if(peopleInParam != null && peopleInParam.getYearMonth() != null && peopleInParam.getRegion() != null &&
                peopleInParam.getClusterId() != null && peopleInParam.getFlag() != null){
            List<Locus> locusList = peopleInSearchService.locusClustering(peopleInParam.getYearMonth().replaceAll("[^\\d]+", ""), peopleInParam.getRegion(),
                    peopleInParam.getClusterId(), peopleInParam.getFlag());

            return ResponseResult.init(locusList);

        } else {
            log.info("[PeopleInController] capturelocus param Error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
    }

}
