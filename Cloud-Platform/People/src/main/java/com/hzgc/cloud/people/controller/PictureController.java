package com.hzgc.cloud.people.controller;

import com.hzgc.cloud.people.param.PictureDTO;
import com.hzgc.cloud.people.param.PictureVO;
import com.hzgc.cloud.people.service.PictureService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/people", tags = "照片库服务")
@Slf4j
public class PictureController {
    @Autowired
    @SuppressWarnings("unused")
    private PictureService pictureService;

    @ApiOperation(value = "添加人口库照片信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.PEOPLE_INSTERT_PICTURE, method = RequestMethod.POST)
    public ResponseResult<Integer> insertPicture(@RequestBody PictureDTO pictureDTO) {
        if (pictureDTO == null) {
            log.error("Start insert picture, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加信息为空，请检查！");
        }
        if (pictureDTO.getType() != 0 && pictureDTO.getType() != 1) {
            log.error("Start insert picture, but type is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "类型选择有误，请检查！");
        }
        if (StringUtils.isBlank(pictureDTO.getPeopleId())) {
            log.error("Start insert picture, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人口ID为空，请检查！");
        }
        if (StringUtils.isBlank(pictureDTO.getPicture())) {
            log.error("Start insert picture, but picture data id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "照片数据为空，请检查！");
        }
        log.info("Start insert picture, param is:" + JacksonUtil.toJson(pictureDTO));
        int status = pictureService.insertPicture(pictureDTO);
        return ResponseResult.init(status);
    }

    @ApiOperation(value = "修改人口库照片信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.PEOPLE_UPDATE_PICTURE, method = RequestMethod.PUT)
    public ResponseResult<Integer> updatePicture(@RequestBody PictureDTO pictureDTO) {
        if (pictureDTO == null) {
            log.error("Start update picture, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改信息为空，请检查！");
        }
        if (pictureDTO.getType() != 0 && pictureDTO.getType() != 1) {
            log.error("Start update picture, but type is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "类型选择有误，请检查！");
        }
        if (pictureDTO.getPictureId() == null) {
            log.error("Start update picture, but picture id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "照片ID为空，请检查！");
        }
        if (StringUtils.isBlank(pictureDTO.getPeopleId())) {
            log.error("Start update picture, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人口ID为空，请检查！");
        }
        if (StringUtils.isBlank(pictureDTO.getPicture())) {
            log.error("Start update picture, but picture data id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "照片数据为空，请检查！");
        }
        log.info("Start update picture, param is:" + JacksonUtil.toJson(pictureDTO));
        int status = pictureService.updatePicture(pictureDTO);
        return ResponseResult.init(status);
    }

    @ApiOperation(value = "删除人口库照片信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.PEOPLE_DELETE_PICTURE, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deletePicture(@RequestBody PictureDTO pictureDTO) {
        if (pictureDTO == null) {
            log.error("Start delete picture, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除信息为空，请检查！");
        }
        if (StringUtils.isBlank(pictureDTO.getPeopleId())) {
            log.error("Start delete picture, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人口ID为空，请检查！");
        }
        if (pictureDTO.getPictureId() == null) {
            log.error("Start delete picture, but picture id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "照片ID为空，请检查！");
        }
        log.info("Start delete picture, param is:" + JacksonUtil.toJson(pictureDTO));
        int status = pictureService.deletePicture(pictureDTO);
        return ResponseResult.init(status);
    }

    @ApiOperation(value = "根据照片ID查询照片", response = byte[].class)
    @RequestMapping(value = BigDataPath.PEOPLE_SEARCH_PICTURE_BY_PICID, method = RequestMethod.GET)
    public ResponseEntity<byte[]> searchPictureByPicId(Long pictureId) {
        if (pictureId == null) {
            log.error("Start select picture, but picture id is null");
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.debug("Start select picture, picture id is:" + pictureId);
        byte[] picture = pictureService.searchPictureByPicId(pictureId);
        if (picture == null || picture.length == 0) {
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
    }

    @ApiOperation(value = "根据人员全局ID查询照片", response = PictureVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SEARCH_PICTURE_BY_PEOPLEID, method = RequestMethod.GET)
    public ResponseResult<PictureVO> searchPictureByPeopleId(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start select picture, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询ID为空，请检查！");
        }
        log.info("Start select picture, people id is:" + peopleId);
        PictureVO pictureVO = pictureService.searchPictureByPeopleId(peopleId);
        log.info("Select picture successfully");
        return ResponseResult.init(pictureVO);
    }
}
