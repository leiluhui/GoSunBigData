package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.people.param.SearchPeopleDTO;
import com.hzgc.service.people.param.*;
import com.hzgc.service.people.service.PeopleService;
import io.swagger.annotations.Api;
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

@RestController
@Api(value = "/people", tags = "人口库服务")
@Slf4j
public class PeopleController {
    @Autowired
    @SuppressWarnings("unused")
    private PeopleService peopleService;

    /**
     * 添加人口
     *
     * @param peopleDTO 人口信息
     * @return 成功状态 1：插入成功, 0：插入失败
     */
    @ApiOperation(value = "添加人口信息", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLE_INSERT, method = RequestMethod.POST)
    public ResponseResult<Integer> insertPeople(@RequestBody PeopleDTO peopleDTO) {
        if (peopleDTO == null) {
            log.error("Start insert people info, but people is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口信息为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getName())) {
            log.error("Start insert people info, but name is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口姓名为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getIdCard())) {
            log.error("Start insert people info, but idCard is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口身份证为空，请检查！");
        }
        boolean boo = peopleService.CheckIdCard(peopleDTO.getIdCard());
        if (boo){
            log.error("Start insert people info, but idCard is exist");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口身份证已存在，请检查！");
        }
        if (peopleDTO.getRegion() == null) {
            log.error("Start insert people info, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口区域为空，请检查！");
        }

        log.info("Start insert people info, param DTO:" + JacksonUtil.toJson(peopleDTO));
        ReturnMessage message = peopleService.insertPeople(peopleDTO);
        if (message != null) {
            if (message.getStatus() == 0) {
                return ResponseResult.error(message.getStatus(), message.getMessage());
            }
            if (message.getStatus() == 1) {
                return ResponseResult.init(1);
            }
        }
        return ResponseResult.error(0, "添加人口失败！");
    }

    /**
     * 修改人口信息
     *
     * @param peopleDTO 人口信息
     * @return 成功状态 1：修改成功, 0：修改失败
     */
    @ApiOperation(value = "修改人口信息", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PEOPLE_UPDATE, method = RequestMethod.PUT)
    public ResponseResult<Integer> updatePeople(@RequestBody PeopleDTO peopleDTO) {
        if (peopleDTO == null) {
            log.error("Start update people info, but people is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口信息为空，请检查！");
        }
        if (peopleDTO.getId() == null) {
            log.error("Start update people info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口ID为空，请检查！");
        }
        if (peopleDTO.getRegion() == null) {
            log.error("Start update people info, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口区域为空，请检查！");
        }
        log.info("Start update people info, param DTO:" + JacksonUtil.toJson(peopleDTO));
        ReturnMessage message = peopleService.updatePeople(peopleDTO);
        if (message != null) {
            if (message.getStatus() == 0) {
                return ResponseResult.error(message.getStatus(), message.getMessage());
            }
            if (message.getStatus() == 1) {
                return ResponseResult.init(1);
            }
        }
        return ResponseResult.error(0, "修改人口失败！");
    }

    /**
     * 根据ID查询人口信息
     *
     * @param peopleId 人员全局ID
     * @return PeopleVO
     */
    @ApiOperation(value = "根据ID查询人口信息", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_BY_PEOPLEID, method = RequestMethod.GET)
    public ResponseResult<PeopleVO> selectByPeopleId(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start select people info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询ID为空，请检查！");
        }
        log.info("Start select people info, people id is:" + peopleId);
        PeopleVO peopleVO = peopleService.selectByPeopleId(peopleId);
        log.info("Select people info successfully, result:" + JacksonUtil.toJson(peopleVO));
        return ResponseResult.init(peopleVO);
    }

    /**
     * 根据照片ID查询照片
     *
     * @param pictureId 照片ID
     * @return byte[] 照片
     */
    @ApiOperation(value = "根据照片ID查询照片", response = byte[].class)
    @RequestMapping(value = BigDataPath.PEOPLE_SEARCH_PICTURE_BY_PICID, method = RequestMethod.GET)
    public ResponseEntity<byte[]> searchPictureByPicId(Long pictureId) {
        if (pictureId == null) {
            log.error("Start select picture, but picture id is null");
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.info("Start select picture, picture id is:" + pictureId);
        byte[] picture = peopleService.searchPictureByPicId(pictureId);
        if (picture == null || picture.length == 0) {
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        log.info("Select picture successfully");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
    }

    /**
     * 根据人员全局ID查询照片
     *
     * @param peopleId 人员全局ID
     * @return PictureVO 照片封装
     */
    @ApiOperation(value = "根据人员全局ID查询照片", response = PictureVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SEARCH_PICTURE_BY_PEOPLEID, method = RequestMethod.GET)
    public ResponseResult<PictureVO> searchPictureByPeopleId(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start select picture, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询ID为空，请检查！");
        }
        log.info("Start select picture, people id is:" + peopleId);
        PictureVO pictureVO = peopleService.searchPictureByPeopleId(peopleId);
        log.info("Slect picture successfully");
        return ResponseResult.init(pictureVO);
    }

    /**
     * 根据身份证查询人员信息
     *
     * @param idCard 身份证
     * @return PeopleVO 人员信息
     */
    /*@ApiOperation(value = "根据身份证查询人员信息", response = PictureVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SEARCH_PICTURE_BY_PEOPLEID, method = RequestMethod.GET)
    public ResponseResult<PeopleVO> searchPeopleByIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            log.error("Start select people info, but idCard is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询ID为空，请检查！");
        }
        boolean boo = peopleService.CheckIdCard(idCard);
        if (boo){
            log.error("Start select people info, but idCard is exist");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口身份证已存在，请检查！");
        }

        return ResponseResult.init(null);
    }*/

    /**
     * 根据条件查询人员
     *
     * @param param 查询条件参数封装
     * @return peopleVO 查询返回参数封装
     */
    @ApiOperation(value = "根据条件查询人员", response = SearchPeopleVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_PEOPLE, method = RequestMethod.POST)
    public ResponseResult<SearchPeopleVO> searchPeople(@RequestBody @ApiParam(value = "查询条件") SearchPeopleDTO param) {
        if (param == null) {
            log.error("Start search people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查!");
        }
        if (param.getRegionId() == null || param.getRegionId() == 0) {
            log.error("Start search people, but region id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "区域ID不能为空,请检查!");
        }
        if (param.getSearchType() != 0 && param.getSearchType() != 1 && param.getSearchType() != 2 && param.getSearchType() != 3) {
            log.error("Start search people, but SearchType is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类型不正确,请检查!");
        }
        log.info("Start search people, search param DTO:" + JacksonUtil.toJson(param));
        FilterField field = FilterField.SearchParamShift(param);
        log.info("Start search people, FilterField param:" + JacksonUtil.toJson(field));
        SearchPeopleVO vo = peopleService.searchPeople(field);
        log.info("Search people successfully, result:" + JacksonUtil.toJson(vo));
        return ResponseResult.init(vo, vo != null ? vo.getTotal() : 0);
    }

    @ApiOperation(value = "统计单个区域下所有小区列表", response = List.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_COMMUNITY, method = RequestMethod.GET)
    public ResponseResult<List<Long>> searchCommunityIdsByRegionId(Long regionId) {
        if (regionId == null) {
            log.info("Start search community id list, but region is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查!");
        }
        log.info("Start search community id list, region is:" + regionId);
        List<Long> communityIds = peopleService.searchCommunityIdsByRegionId(regionId);
        log.info("Search community id list successfully, result:" + JacksonUtil.toJson(communityIds));
        return ResponseResult.init(communityIds);
    }
}
