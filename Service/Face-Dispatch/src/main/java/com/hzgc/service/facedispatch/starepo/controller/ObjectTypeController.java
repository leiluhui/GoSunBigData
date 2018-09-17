package com.hzgc.service.facedispatch.starepo.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.service.facedispatch.starepo.model.ObjectType;
import com.hzgc.service.facedispatch.starepo.model.ObjectTypeDTO;
import com.hzgc.service.facedispatch.starepo.model.ObjectTypeVO;
import com.hzgc.service.facedispatch.starepo.service.ObjectTypeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(value = "/objectType", tags = "对象类型")
public class ObjectTypeController {

    @Autowired
    private ObjectTypeService objectTypeService;

    /**
     * 添加对象类型
     *
     * @param param objectType
     * @return 0：插入成功；1：插入失败
     */
    @ApiOperation(value = "添加对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_ADD, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseResult <Integer> addObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectTypeDTO param) {
        if (param == null) {
            log.error("Start add object type, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象类型信息为空，请检查！");
        }
        String name = param.getName();
        if (StringUtils.isBlank(name)) {
            log.error("Start add object type, but object type name is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象类型名称为空，请检查！");
        }
        boolean isExists_objectTypeName = objectTypeService.isExists_objectTypeName(name);
        if (isExists_objectTypeName) {
            log.error("Start add object type, but the object type name already exists");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加对象类型名称已存在，请检查！");
        }
        log.info("Start update object type, DTO : " + JacksonUtil.toJson(param));
        ObjectType objectType = param.objectTypeDTOShift(param);
        objectType.setId("type_" + UuidUtil.getUuid().substring(0, 8));
        log.info("Start add object type, param is:" + JacksonUtil.toJson(param));
        int add = objectTypeService.addObjectType(objectType);
        return ResponseResult.init(add);
    }

    /**
     * 删除objectType
     *
     * @param idList 类型Key列表
     * @return boolean
     */
    @ApiOperation(value = "删除对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_DELETE, method = RequestMethod.DELETE)
    public ResponseResult <Integer> deleteObjectType(@RequestBody @ApiParam(value = "对象类型key列表") List <String> idList) {
        if (idList == null || idList.size() == 0) {
            log.error("Start delete object type list, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除列表为空，请检查!");
        }
        log.info("Start delete object type list, param is : " + idList);
        int status = objectTypeService.deleteObjectType(idList);
        if (status == idList.size()) {
            log.info("Delete object type successfully");
            return ResponseResult.init(status);
        } else {
            log.info("Delete object type failed");
            return ResponseResult.error(status, "删除对象失败");
        }
    }

    /**
     * 修改ObjectType
     *
     * @param param update objectType对象
     * @return boolean
     */
    @ApiOperation(value = "修改对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_UPDATE, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseResult <Integer> updateObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectTypeDTO param) {
        if (param == null) {
            log.error("Start update object type, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型信息为空，请检查！");
        }
        String typeId = param.getId();
        if (StringUtils.isBlank(typeId)) {
            log.error("Start update object type, but object type key is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型ID为空，请检查！");
        }
        String name = param.getName();
        if (StringUtils.isBlank(name)) {
            log.error("Start update object type, but object type name is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型名称为空，请检查！");
        }
        boolean isExists_objectTypeName = objectTypeService.isExists_objectTypeName(name);
        if (isExists_objectTypeName) {
            log.error("Start update object type, but the object type name already exists");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象类型名称已存在，请检查！");
        }
        log.info("Start update object type, DTO : " + JacksonUtil.toJson(param));
        ObjectType objectType = param.objectTypeDTOShift(param);
        log.info("Start update object type, param is : " + JacksonUtil.toJson(objectType));
        int status = objectTypeService.updateObjectType(objectType);
        if (status == 1) {
            log.info("Update object type successfully");
            return ResponseResult.init(status);
        } else {
            log.info("Update object type failed");
            return ResponseResult.error(status, "删除对象失败");
        }
    }

    /**
     * 查询objectType
     *
     * @param start 起始页码
     * @param limit 每页行数
     * @return List<ObjectType>
     */
    @ApiOperation(value = "查询对象类型", response = ResponseResult.class)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", value = "起始行数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页行数", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.TYPE_SEARCH, method = RequestMethod.GET)
    public ResponseResult <List <ObjectTypeVO>> searchObjectType(Integer start, Integer limit) {
        log.info("Start search object type, param is : start = " + start + "; end = " + limit);
        List <ObjectTypeVO> list = objectTypeService.searchObjectType(start, limit);
        int count = objectTypeService.countObjectType();
        return ResponseResult.init(list, count);
    }

    /**
     * 根据Id查询对象类型名称
     *
     * @param objectTypeIds 对象类型key数组
     * @return Map
     */
    @ApiOperation(value = "查询对象类型名称", response = ResponseResult.class)
    @ApiImplicitParam(name = "objectTypeKeys", value = "对象类型key数组", dataType = "List", paramType = "query")
    @RequestMapping(value = BigDataPath.TYPE_SEARCH_NAMES, method = RequestMethod.POST)
    public ResponseResult <Map> searchObjectTypeNames(@RequestBody List <String> objectTypeIds) {
        if (objectTypeIds == null || objectTypeIds.size() <= 0) {
            log.error("Start search object type names, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start search object type names, param is : " + objectTypeIds);
        Map map = objectTypeService.searchObjectTypeNames(objectTypeIds);
        return ResponseResult.init(map);
    }
}
