package com.hzgc.service.clustering.controller;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.export.*;
import com.hzgc.service.clustering.bean.param.GetResidentParam;
import com.hzgc.service.clustering.bean.param.ResidentParam;
import com.hzgc.service.clustering.service.ClusteringSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@Api(tags = "人口实名制管理")
public class ResidentController {

    @Autowired
    private ClusteringSearchService clusteringSearchService;

    /**
     * 计划保存
     *
     * @param regular 计划保存
     */
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_SAVEPLAN, method = RequestMethod.POST)
    public ResponseResult<Integer> saveRegular(@RequestBody @ApiParam(value = "实名制规则存储") Regular regular) {
        if (regular == null) {
            log.error("Start save plan,but the rules is empty!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "regular is null!");
        }
        if (regular.getRegionID() == null || regular.getRegionName() == null || regular.getSim() == null || regular.getMoveInCount() == null
                || regular.getMoveOutDays() == null) {
            log.error("Start save plan, but the param is error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "Param is empty or error");
        }
        Boolean isExists_region = clusteringSearchService.isExists_region(regular);
        if (!isExists_region){
            log.error("Start save plan, but the region id is exists,please check params!!!");
        }
        log.info("Starting realname param : " + JSONUtil.toJson(regular));
        Integer succeed = clusteringSearchService.saveRegular(regular);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "保存计划失败！");
        }
    }

    /**
     * 计划查询
     *
     * @return 返回结果封装类中
     */
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_SEARCHPLAN, method = RequestMethod.GET)
    public ResponseResult<List> searchPlan(String regionID) {
        List<Regular> regularList = clusteringSearchService.searchPlan(regionID);
        return ResponseResult.init(regularList);
    }

    /**
     * 计划修改
     *
     * @param regular
     * @return 返回是否成功的标识 【0：修改成功；1：修改失败】
     */
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_MODIFYPLAN, method = RequestMethod.POST)
    public ResponseResult<Integer> modifyPlan(@RequestBody @ApiParam(value = "实名制规则存储") Regular regular) {
        String regionID = regular.getRegionID();
        String sim = regular.getSim();
        String moveInCount = regular.getMoveInCount();
        String moveOutDays = regular.getMoveOutDays();
        if (regionID == null || sim == null || moveInCount == null || moveOutDays == null) {
            log.error("Start to modify the plan, but the region or sim is empty !!!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改计划区域或者相似度为空，请检查！！");
        }
        Integer succeed = clusteringSearchService.modifyPlan(regionID, sim, moveInCount, moveOutDays);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "修改计划失败");
        }
    }

    /**
     * 计划删除
     *
     * @param regionID 根据区域删除计划
     * @return 返回是否成功的标识 【0：删除成功；1：删除失败】
     */
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_DELETEPLAN, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deletePlan(@RequestBody @ApiParam(value = "删除计划") List<String> regionID) {
        if (regionID == null || regionID.size() == 0) {
            log.error("Start to delete plan,but the region list is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除区域为空，请检查！");
        }
        log.info("Start to delete plan,the region list is: " + regionID);
        Integer succeed = clusteringSearchService.deletePlan(regionID);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "删除计划失败！");
        }
    }

    /**
     * 添加对象
     *
     * @param param 对象信息
     * @return 成功状态【0：插入成功；1：插入失败】
     */
    @ApiOperation(value = "添加对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_ADDPERSON, method = RequestMethod.POST)
    public ResponseResult<Integer> addPerson(@RequestBody @ApiParam(value = "添加常驻人口") ResidentParam param) {
        if (param == null) {
            log.error("Start add person,but param is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象人口信息为空，请检查！");
        }
        if (StringUtils.isBlank(param.getRegionID())) {
            log.error("Start add person, but the region is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象区域ID为空，请检查！");
        }
        if (param.getPictureDatas() == null || param.getPictureDatas().getImageData() == null) {
            log.error("Start add person,but the picture data is empty!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象照片数据为空，请检查！");
        }
        boolean authentication_idCode = clusteringSearchService.authentication_idCode(param);
        if (!authentication_idCode) {
            log.error("Start add person,but the idcard format is error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证格式错误，请检查！");
        }
        boolean isExists_idCode = clusteringSearchService.isExists_idCode(param);
        if (isExists_idCode) {
            log.error("Start add person, but the idcard already exists!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象身份证已存在，请检查！");
        }
        log.info("Start add person,param is : " + JSONUtil.toJson(param));
        Integer succeed = clusteringSearchService.addPerson(param);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "添加对象失败！");
        }
    }

    /**
     * 删除对象
     *
     * @param rowkeyList 对象ID列表
     * @return 成功状态【0：删除成功；1：删除失败】
     */
    @ApiOperation(value = "删除对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_DELETEPERSON, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deletePerson(@RequestBody @ApiParam(value = "删除列表") List<String> rowkeyList) {
        if (rowkeyList == null || rowkeyList.size() == 0) {
            log.error("Start delete object info, but rowkey list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除列表为空，请检查！");
        }
        log.info("Start delete object info, rowkey list is:" + rowkeyList);
        Integer succeed = clusteringSearchService.deletePerson(rowkeyList);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "删除对象失败");
        }
    }

    /**
     * 修改对象
     *
     * @param param 对象信息
     * @return 成功状态【0：插入成功；1：插入失败】
     */
    @ApiOperation(value = "修改对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_MODIFYPERSON, method = RequestMethod.PUT)
    public ResponseResult<Integer> updatePerson(@RequestBody @ApiParam(value = "修改对象") ResidentParam param) {
        if (param == null) {
            log.error("Start update person, but the param is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象为空，请检查！");
        }
        if (StringUtils.isBlank(param.getId())) {
            log.error("Start update person, but the id of person is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象ID为空，请检查！");
        }
        boolean authentication_idCode = clusteringSearchService.authentication_idCode(param);
        if (!authentication_idCode) {
            log.error("Start update person, but the idcard of person is error!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象身份证格式错误，请检查！");
        }
        String idCade_DB = clusteringSearchService.getObjectIdCard(param);
        if (!StringUtils.isBlank(param.getIdcard()) && !param.getIdcard().equals(idCade_DB)) {
            boolean isExists_idCode = clusteringSearchService.isExists_idCode(param);
            if (isExists_idCode) {
                log.error("Start update person,but the idcard is already exists!");
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象身份证ID已存在，请检查！");
            }
        }
        log.info("Start update person,param is : " + JSONUtil.toJson(param));
        Integer succeed = clusteringSearchService.updatePerson(param);
        if (succeed == 0) {
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(succeed, "修改对象失败！");
        }
    }


    /**
     * 根据id查询对象
     *
     * @param objectId 对象ID
     * @return ObjectInfo
     */
    @ApiOperation(value = "根据id查询对象", response = ResponseResult.class)
    @ApiImplicitParam(name = "objectId", value = "对象ID", dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_GETPERSON, method = RequestMethod.GET)
    public ResponseResult<Resident> getPerson(String objectId) {
        if (StringUtils.isBlank(objectId)) {
            log.error("Start to get person,but object id is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询对象ID为空，请检查！");
        }
        log.info("Start to get person,param is : " + objectId);
        Resident resident = clusteringSearchService.getPerson(objectId);
        return ResponseResult.init(resident);
    }

    /**
     * 获取常驻人口库照片
     *
     * @param objectID 对象ID
     *                 return byte[]
     */
    @ApiOperation(value = "获取常驻人口库照片", produces = "image/jpeg")
    @ApiImplicitParam(name = "objectID", value = "对象ID", dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_GETRESIDENTPICTURE, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getResidentPhoto(String objectID) {
        if (StringUtils.isBlank(objectID)) {
            log.error("Start to get object photo,but the objectID is null!");
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.info("Start to get the object photo, param is : " + objectID);
        byte[] photo = clusteringSearchService.getResidentPhoto(objectID);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
    }

    /**
     * 查询对象
     *
     * @param param 查询条件封装
     * @return
     */
    @ApiOperation(value = "对象查询", response = ResidentSearchResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_RESIDENTSEARCH, method = RequestMethod.POST)
    public ResponseResult<List> searchResident(@RequestBody @ApiParam(value = "查询条件") GetResidentParam param) {
        if (param == null) {
            log.error("Start get resident,but param is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询人口对象参数为空，请检查！");
        }
        log.info("Start get resident,param is : " + JSONUtil.toJson(param));
        Map<List<PersonObject>,Integer> result = clusteringSearchService.searchResident(param);
        Set<List<PersonObject>> set = result.keySet();
        List<PersonObject> personObjects = null;
        int count = 0;
        for (List<PersonObject> list : set){
            personObjects = list;
            count = result.get(list);
        }
        return ResponseResult.init(personObjects, count);
    }


    /**
     * 抓拍次数查询
     *
     * @param rowkeyList 常驻人口库中某个人的ID
     * @reture map 返回这个人的抓拍次数的key-value对
     */
    @ApiOperation(value = "根据id查询抓拍次数", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_CAPTURECOUNT, method = RequestMethod.POST)
    public ResponseResult<Map> getCaptureCount(@RequestBody @ApiParam(value = "id列表") RowkeyList rowkeyList) {
        List<String> rowkeylist = rowkeyList.getRowkeyList();
        if (rowkeylist == null || rowkeylist.size() == 0) {
            log.error("Start to get capture count, but the rowkeyList is null!");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询rowkey为空，请检查！");
        }
        log.info("Start to get capture count,param is : " + rowkeylist);
        Map<String, Integer> map = clusteringSearchService.getCaptureCount(rowkeylist);
        return ResponseResult.init(map);
    }

    /**
     * 抓拍历史查询
     *
     * @param rowkeyList 常驻人口库ID的list
     * @return 返回一个人的抓拍历史
     */
    @ApiOperation(value = "根据id查询抓拍历史", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_CAPTUREHISTORY, method = RequestMethod.POST)
    public ResponseResult<Map> getCaptureHistory(@RequestBody @ApiParam(value = "id列表") RowkeyList rowkeyList) {
        List<String> rowkeylist = rowkeyList.getRowkeyList();
        if (rowkeylist == null || rowkeylist.size() == 0) {
            log.error("Start to get capture history, but the rowkeyList is null!");
        }
        log.info("Start to get capture history, param is : " + rowkeyList);
        Map<String, List<FaceObject>> map = clusteringSearchService.getCaptureHistory(rowkeyList);
        return ResponseResult.init(map);
    }

    /**
     * 抓拍历史轨迹
     */
    @ApiOperation(value = "根据id查询抓拍历史轨迹",response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLEMANAGER_CAPTURELOCUS,method = RequestMethod.POST)
    public ResponseResult<List> getCaptureLocus(@RequestBody @ApiParam(value = "id")RowkeyList rowkeyList){
        List<String> rowkeylist = rowkeyList.getRowkeyList();
        if (rowkeylist == null || rowkeylist.size() == 0) {
            log.error("Start to get capture locus, but the rowkeyList is null!");
        }
        List<CapatureLocus> capatureLocusList = clusteringSearchService.getCaptureLocus(rowkeylist);
        return ResponseResult.init(capatureLocusList);
    }
}














