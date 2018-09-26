package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.people.param.SearchParamDTO;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.*;
import com.hzgc.service.people.service.PeopleService;
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

@RestController
@Api(value = "/people", tags = "人口库服务")
@Slf4j
public class PeopleController {
    @Autowired
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
            log.error("Start insert people info, but idcard is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口身份证为空，请检查！");
        }
        if (peopleDTO.getRegion() == null) {
            log.error("Start insert people info, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口区域为空，请检查！");
        }

        log.info("Start add people info, param DTO:" + JacksonUtil.toJson(peopleDTO));
        People people = peopleDTO.peopleDTOShift_insert(peopleDTO);
        log.info("Start add object info, param is:" + JacksonUtil.toJson(people));
        Integer status = peopleService.people_insert(people);
        if (status == null || status != 1) {
            log.info("Insert people to t_people info failed");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口信息失败");
        }
        log.info("Insert t_people info successfully");
        if (peopleDTO.getFlagId() != null || peopleDTO.getIdCardPic() != null || peopleDTO.getCapturePic() != null ||
                peopleDTO.getImsi() != null || peopleDTO.getPhone() != null || peopleDTO.getHouse() != null ||
                peopleDTO.getCar() != null) {
            if (peopleDTO.getFlagId() != null && peopleDTO.getFlagId().size() > 0) {
                Integer insertStatus = peopleService.people_flag_insert(people.getId(), peopleDTO.getFlagId());
                if (insertStatus == 1) {
                    log.info("Insert flag to t_flag successfully");
                } else {
                    log.info("Insert flag to t_flag failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口标签表失败");
                }
            }
            if (peopleDTO.getIdCardPic() != null && peopleDTO.getIdCardPic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_insert(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic());
                if (insertStatus == 1) {
                    log.info("Insert idCard pic to t_picture successfully");
                } else {
                    log.info("Insert idCard pic to t_picture failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口证件照片表失败");
                }
            }
            if (peopleDTO.getCapturePic() != null && peopleDTO.getCapturePic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_insert(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic());
                if (insertStatus == 1) {
                    log.info("Insert capture pic to t_picture successfully");
                } else {
                    log.info("Insert capture pic to t_picture failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口实采照片表失败");
                }
            }
            if (peopleDTO.getImsi() != null && peopleDTO.getImsi().size() > 0) {
                Integer insertStatus = peopleService.people_imsi_insert(people.getId(), peopleDTO.getImsi());
                if (insertStatus == 1) {
                    log.info("Insert imsi to t_imsi successfully");
                } else {
                    log.info("Insert imsi to t_imsi failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口imsi表失败");
                }
            }
            if (peopleDTO.getPhone() != null && peopleDTO.getPhone().size() > 0) {
                Integer insertStatus = peopleService.people_phone_insert(people.getId(), peopleDTO.getPhone());
                if (insertStatus == 1) {
                    log.info("Insert phone to t_phone successfully");
                } else {
                    log.info("Insert phone to t_phone failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口联系方式表失败");
                }
            }
            if (peopleDTO.getHouse() != null && peopleDTO.getHouse().size() > 0) {
                Integer insertStatus = peopleService.people_house_insert(people.getId(), peopleDTO.getHouse());
                if (insertStatus == 1) {
                    log.info("Insert house to t_house successfully");
                } else {
                    log.info("Insert house to t_house failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口房产信息表失败");
                }
            }
            if (peopleDTO.getCar() != null && peopleDTO.getCar().size() > 0) {
                Integer insertStatus = peopleService.people_car_insert(people.getId(), peopleDTO.getCar());
                if (insertStatus == 1) {
                    log.info("Insert car to t_car successfully");
                } else {
                    log.info("Insert car to t_car failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口车辆信息表失败");
                }
            }
        }
        log.info("Insert people info successfully");
        return ResponseResult.init(1);
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
        People people = peopleDTO.peopleDTOShift_update(peopleDTO);
        log.info("Start update object info, param is:" + JacksonUtil.toJson(people));
        Integer status = peopleService.people_update(people);
        if (status == null || status != 1) {
            log.info("Update t_people info failed");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口失败");
        }
        log.info("Update t_people info successfully");
        if (peopleDTO.getFlagId() != null || peopleDTO.getIdCardPic() != null || peopleDTO.getCapturePic() != null ||
                peopleDTO.getImsi() != null || peopleDTO.getPhone() != null || peopleDTO.getHouse() != null ||
                peopleDTO.getCar() != null) {
            if (peopleDTO.getFlagId() != null && peopleDTO.getFlagId().size() > 0) {
                Integer insertStatus = peopleService.people_flag_update(people.getId(), peopleDTO.getFlagId());
                if (insertStatus == 1) {
                    log.info("Update flag to t_flag successfully");
                } else {
                    log.info("Update flag to t_flag failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口标签表失败");
                }
            }
            if (peopleDTO.getIdCardPic() != null && peopleDTO.getIdCardPic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_update(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic());
                if (insertStatus == 1) {
                    log.info("Update idCard pic to t_picture successfully");
                } else {
                    log.info("Update idCard pic to t_picture failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口证件照片表失败");
                }
            }
            if (peopleDTO.getCapturePic() != null && peopleDTO.getCapturePic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_update(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic());
                if (insertStatus == 1) {
                    log.info("Update capture pic to t_picture successfully");
                } else {
                    log.info("Update capture pic to t_picture failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口实采照片表失败");
                }
            }
            if (peopleDTO.getImsi() != null && peopleDTO.getImsi().size() > 0) {
                Integer insertStatus = peopleService.people_imsi_update(people.getId(), peopleDTO.getImsi());
                if (insertStatus == 1) {
                    log.info("Update imsi to t_imsi successfully");
                } else {
                    log.info("Update imsi to t_imsi failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口imsi表失败");
                }
            }
            if (peopleDTO.getPhone() != null && peopleDTO.getPhone().size() > 0) {
                Integer insertStatus = peopleService.people_phone_update(people.getId(), peopleDTO.getPhone());
                if (insertStatus == 1) {
                    log.info("Update phone to t_phone successfully");
                } else {
                    log.info("Update phone to t_phone failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口联系方式表失败");
                }
            }
            if (peopleDTO.getHouse() != null && peopleDTO.getHouse().size() > 0) {
                Integer insertStatus = peopleService.people_house_update(people.getId(), peopleDTO.getHouse());
                if (insertStatus == 1) {
                    log.info("Update house to t_house successfully");
                } else {
                    log.info("Update house to t_house failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口房产信息表失败");
                }
            }
            if (peopleDTO.getCar() != null) {
                Integer insertStatus = peopleService.people_car_update(people.getId(), peopleDTO.getCar());
                if (insertStatus == 1) {
                    log.info("Update car to t_car successfully");
                } else {
                    log.info("Update car to t_car failed");
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口车辆信息表失败");
                }
            }
        }
        log.info("Update people info successfully");
        return ResponseResult.init(1);
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
    public ResponseResult<byte[]> searchPictureByPicId(Long pictureId) {
        if (pictureId != null) {
            log.error("Start select picture, but picture id is null");
        }
        log.info("Start select picture, picture id is:" + pictureId);
        byte[] pic = peopleService.searchPictureByPicId(pictureId);
        log.info("Select picture successfully");
        return ResponseResult.init(pic);
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
        }
        log.info("Start select picture, people id is:" + peopleId);
        PictureVO pictureVO = peopleService.searchPictureByPeopleId(peopleId);
        log.info("Slect picture successfully");
        return ResponseResult.init(pictureVO);
    }

    /**
     * 根据条件查询人员
     *
     * @param param 查询条件参数封装
     * @return peopleVO 查询返回参数封装
     */
    @ApiOperation(value = "根据条件查询人员", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_PEOPLE, method = RequestMethod.POST)
    public ResponseResult<List<PeopleVO>> searchPeople(@RequestBody @ApiParam(value = "查询条件") SearchParamDTO param) {
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
        List<PeopleVO> peoples = peopleService.searchPeople(field);
        log.info("Search people successfully, result:" + JacksonUtil.toJson(peoples));
        return ResponseResult.init(peoples);
    }

    @ApiOperation(value = "统计单个区域下所有小区列表", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_COMMUNITY, method = RequestMethod.GET)
    public ResponseResult<List<Long>> searchCommunityIdsByRegionId(Long regionId) {
        if (regionId == null) {
            log.info("start search community id list, but region is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查!");
        }
        log.info("Start search community id list, region is:" + regionId);
        List<Long> communityIds = peopleService.searchCommunityIdsByRegionId(regionId);
        log.info("Search community id list successfully, result:" + JacksonUtil.toJson(communityIds));
        return ResponseResult.init(communityIds);
    }
}
