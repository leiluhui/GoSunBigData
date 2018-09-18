package com.hzgc.service.facedispatch.starepo.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.PictureData;
import com.hzgc.service.facedispatch.starepo.bean.GetObjectInfoParam;
import com.hzgc.service.facedispatch.starepo.bean.ObjectSearchResult;
import com.hzgc.service.facedispatch.starepo.model.ObjectInfo;
import com.hzgc.service.facedispatch.starepo.model.ObjectInfoDTO;
import com.hzgc.service.facedispatch.starepo.model.ObjectInfoVO;
import com.hzgc.service.facedispatch.starepo.service.FaceSearchService;
import com.hzgc.service.facedispatch.starepo.service.ObjectInfoService;
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

@Slf4j
@RestController
@Api(tags = "静态库服务")
public class ObjectInfoController {
    @Autowired
    @SuppressWarnings("unused")
    private ObjectInfoService objectInfoService;

    @Autowired
    @SuppressWarnings("unused")
    private FaceSearchService faceSearchService;

    /**
     * 添加对象
     *
     * @param param 对象信息
     * @return 成功状态【0：插入成功；1：插入失败】
     */
    @ApiOperation(value = "添加对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.OBJECTINFO_ADD, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseResult <Integer> addObjectInfo(@RequestBody @ApiParam(value = "添加对象") ObjectInfoDTO param) {
        if (param == null) {
            log.error("Start add object info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象信息为空，请检查！");
        }
        String typeId = param.getTypeid();
        if (StringUtils.isBlank(typeId)) {
            log.error("Start add object info ,but object type id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象类型为空，请检查！");
        }
        boolean isExists_objectTypeId = objectInfoService.isExists_objectTypeId(typeId);
        if (!isExists_objectTypeId) {
            log.error("Start add object info, but the object type key doesn't exists");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象类型不存在，请检查！");
        }
        if (param.getPicture() == null || param.getPicture().getImageData() == null) {
            log.error("Start add object info, but image data is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象照片数据为空，请检查！");
        }
        String idCard = param.getIdcard();
        if (StringUtils.isNotBlank(idCard)) {
            boolean authentication_idCard = objectInfoService.authentication_idCard(idCard);
            if (!authentication_idCard) {
                log.error("Start add object info, but the idcard format is error");
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "身份证格式错误，请检查！");
            }
            boolean isExists_idCard = objectInfoService.isExists_idCard(idCard);
            if (isExists_idCard) {
                log.error("Start add object info, but the idcard already exists");
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "身份证已存在，请检查！");
            }
        }
        log.info("Start add object info, DTO :" + JacksonUtil.toJson(param));
        ObjectInfo objectInfo = param.objectInfoDTOShift(param);
        objectInfo.setId(UuidUtil.getUuid());
        log.info("Start add object info, param is:" + JacksonUtil.toJson(objectInfo));
        Integer succeed = objectInfoService.addObjectInfo(objectInfo);
        if (succeed == 0) {
            log.info("Add object info successfully");
            return ResponseResult.init(succeed);
        } else {
            log.info("Add object info failed");
            return ResponseResult.error(succeed, "添加对象失败");
        }
    }

    /**
     * 删除对象
     *
     * @param idList 对象ID列表
     * @return 成功状态【0：删除成功；1：删除失败】
     */
    @ApiOperation(value = "删除对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.OBJECTINFO_DELETE, method = RequestMethod.DELETE)
    public ResponseResult <Integer> deleteObjectInfo(@RequestBody @ApiParam(value = "删除列表") List <String> idList) {
        if (idList == null || idList.size() == 0) {
            log.error("Start delete object info, but object id list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除列表为空，请检查！");
        }
        log.info("Start delete object info, object id list is:" + JacksonUtil.toJson(idList));
        Integer status = objectInfoService.deleteObjectInfo(idList);
        if (status == idList.size()) {
            log.info("Delete object info successfully");
            return ResponseResult.init(status);
        } else {
            log.info("Delete object info failed");
            return ResponseResult.error(status, "删除对象失败");
        }
    }

    /**
     * 修改对象
     *
     * @param param 对象信息
     * @return 成功状态【0：插入成功；1：插入失败】
     */
    @ApiOperation(value = "修改对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.OBJECTINFO_UPDATE, method = RequestMethod.PUT)
    public ResponseResult <Integer> updateObjectInfo(@RequestBody @ApiParam(value = "修改对象") ObjectInfoDTO param) {
        if (param == null) {
            log.error("Start update object info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象为空，请检查！");
        }
        if (StringUtils.isBlank(param.getId())) {
            log.error("Start update object info, but object id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象ID为空，请检查！");
        }
        String typeId = param.getTypeid();
        if (StringUtils.isBlank(typeId)) {
            log.error("Start update object info, but object type id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型为空，请检查！");
        }
        boolean isExists_objectTypeId = objectInfoService.isExists_objectTypeId(typeId);
        if (!isExists_objectTypeId) {
            log.error("Start update object info, but the object type id doesn't exists");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型不存在，请检查！");
        }
        String idCard = param.getIdcard();
        if (StringUtils.isNotBlank(idCard)) {
            boolean authentication_idCard = objectInfoService.authentication_idCard(idCard);
            if (!authentication_idCard) {
                log.error("Start add object info, but the idcard format is error");
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "身份证格式错误，请检查！");
            }
            boolean isExists_idCard = objectInfoService.isExists_idCard(idCard);
            if (isExists_idCard) {
                log.error("Start add object info, but the idcard already exists");
                return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "身份证已存在，请检查！");
            }
        }
        log.info("Start update object info, DTO :" + JacksonUtil.toJson(param));
        ObjectInfo objectInfo = param.objectInfoDTOShift(param);
        log.info("Start update object info, param is:" + JacksonUtil.toJson(param));
        Integer succeed = objectInfoService.updateObjectInfo(objectInfo);
        if (succeed == 1) {
            log.info("Update object info successfully");
            return ResponseResult.init(succeed);
        } else {
            log.info("Update object info failed");
            return ResponseResult.error(succeed, "修改对象失败");
        }
    }

    /**
     * 根据id查询对象
     *
     * @param objectId 对象ID
     * @return ObjectInfoVo
     */
    @ApiOperation(value = "根据id查询对象", response = ResponseResult.class)
    @ApiImplicitParam(name = "objectId", value = "对象ID", dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.OBJECTINFO_GET, method = RequestMethod.GET)
    public ResponseResult <ObjectInfoVO> getObjectInfo(String objectId) {
        if (StringUtils.isBlank(objectId)) {
            log.error("Start get object info, but object id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询对象ID为空，请检查！");
        }
        log.info("Start get object info, param is : " + objectId);
        ObjectInfoVO objectInfoVo = objectInfoService.getObjectInfo(objectId);
        log.info("Get object info successfully, result : " + JacksonUtil.toJson(objectInfoVo));
        return ResponseResult.init(objectInfoVo);
    }

    /**
     * 根据id查询对象照片
     *
     * @param objectID 对象ID
     * @return byte[] 对象照片
     */
    @ApiOperation(value = "获取静态库照片", produces = "image/jpeg")
    @ApiImplicitParam(name = "objectID", value = "对象ID", dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.OBJECTINFO_GET_PHOTOBYKEY, method = RequestMethod.GET)
    public ResponseEntity <byte[]> getObjectPhoto(String objectID) {
        if (StringUtils.isBlank(objectID)) {
            log.error("Start get object photo, but object id null");
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.info("Start get object photo, param is : " + objectID);
        byte[] photo = objectInfoService.getObjectPhoto(objectID);
        log.info("Get object info photo successfully");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
    }

    /**
     * 根据id查询对象特征值
     *
     * @param id 对象ID
     * @return PictureData
     */
    @ApiOperation(value = "获取特征值<PictureData>", response = PictureData.class)
    @ApiImplicitParam(name = "id", value = "对象ID", dataType = "String", paramType = "query")
    @RequestMapping(value = BigDataPath.OBJECTINFO_GET_FEATURE, method = RequestMethod.GET)
    public ResponseResult <PictureData> getFeature(String id) {
        if (StringUtils.isBlank(id)) {
            log.error("Start get object picture data, but object id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询对象ID为空，请检查！");
        }
        log.info("Start get object picture data, param is : " + id);
        PictureData pictureData = objectInfoService.getFeature(id);
        log.info("Get object pictureData successfully");
        return ResponseResult.init(pictureData);
    }

    /**
     * 查询对象（以图搜图）
     *
     * @param param 查询条件封装
     * @return ObjectSearchResult
     */
    @ApiOperation(value = "对象查询", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.OBJECTINFO_SEARCH, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> searchObjectInfo(@RequestBody @ApiParam(value = "查询条件") GetObjectInfoParam param) {
        if (param == null) {
            log.error("Start get object info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询对象参数为空，请检查！");
        }
        log.info("Start get object info, param is:" + JacksonUtil.toJson(param));
        ObjectSearchResult result = faceSearchService.search(param);
        return ResponseResult.init(result);
    }
}
