package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.service.rest.BigDataPermission;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.FilterField;
import com.hzgc.service.people.param.PeopleDTO;
import com.hzgc.service.people.param.PeopleVO;
import com.hzgc.service.people.param.SearchParam;
import com.hzgc.service.people.service.PeopleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 添加人口对象
     *
     * @param peopleDTO 人口对象信息
     * @return 成功状态【0：插入成功；1：插入失败】
     */
    @ApiOperation(value = "添加人口对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.ADD_PEOPLE, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.OBJECT_OPERATION + "')")
    public ResponseResult<Integer> insertPeople(@RequestBody @ApiParam(value = "添加人口对象") PeopleDTO peopleDTO) {
        if (peopleDTO == null) {
            log.error("Start Insert people info, but people is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口对象信息为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getName())) {
            log.error("Start Insert people info, but name is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口对象姓名为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getIdcard())) {
            log.error("Start Insert people info, but idcard is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口对象身份证为空，请检查！");
        }
        if (peopleDTO.getRegion() == null) {
            log.error("Start Insert people info, but region is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加人口对象区域为空，请检查！");
        }
        log.info("Start add people info, DTO :" + JSONUtil.toJson(peopleDTO));
        People people = peopleDTO.peopleDTOShift(peopleDTO);
        log.info("Start add object info, param is:" + JSONUtil.toJson(people));
        Integer status = peopleService.people(people, PeopleService.INSERT);
        if (status == null || status != 1) {
            log.info("Insert t_people info failed");
            return ResponseResult.error(0, "添加人口对象失败");
        }
        log.info("Insert t_people info successfully");
        if (peopleDTO.getFlag() == null && peopleDTO.getIdCardPic() == null && peopleDTO.getCapturePic() == null &&
                peopleDTO.getImsi() == null && peopleDTO.getPhone() == null && peopleDTO.getHouse() == null &&
                peopleDTO.getCar() == null) {
            log.info("Insert people info successfully");
            return ResponseResult.init(status);
        } else {
            if (peopleDTO.getFlag() != null && peopleDTO.getFlag().size() > 0) {
                Integer insertStatus = peopleService.people_flag(people.getId(), peopleDTO.getFlag(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert flag to t_flag successfully");
                } else {
                    log.info("Insert flag to t_flag failed");
                    return ResponseResult.error(0, "添加人口对象标签表失败");
                }
            }
            if (peopleDTO.getIdCardPic() != null && peopleDTO.getIdCardPic().size() > 0) {
                Integer insertStatus = peopleService.people_picture(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert idCard pic to t_picture successfully");
                } else {
                    log.info("Insert idCard pic to t_picture failed");
                    return ResponseResult.error(0, "添加人口对象证件照片表失败");
                }
            }
            if (peopleDTO.getCapturePic() != null) {
                Integer insertStatus = peopleService.people_picture(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert capture pic to t_picture successfully");
                } else {
                    log.info("Insert capture pic to t_picture failed");
                    return ResponseResult.error(0, "添加人口对象实采照片表失败");
                }
            }
            if (peopleDTO.getImsi() != null) {
                Integer insertStatus = peopleService.people_imsi(people.getId(), peopleDTO.getImsi(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert imsi to t_imsi successfully");
                } else {
                    log.info("Insert imsi to t_imsi failed");
                    return ResponseResult.error(0, "添加人口对象imsi表失败");
                }
            }
            if (peopleDTO.getPhone() != null) {
                Integer insertStatus = peopleService.people_phone(people.getId(), peopleDTO.getPhone(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert phone to t_phone successfully");
                } else {
                    log.info("Insert phone to t_phone failed");
                    return ResponseResult.error(0, "添加人口对象联系方式表失败");
                }
            }
            if (peopleDTO.getHouse() != null) {
                Integer insertStatus = peopleService.people_house(people.getId(), peopleDTO.getHouse(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert house to t_house successfully");
                } else {
                    log.info("Insert house to t_house failed");
                    return ResponseResult.error(0, "添加人口对象房产信息表失败");
                }
            }
            if (peopleDTO.getCar() != null) {
                Integer insertStatus = peopleService.people_car(people.getId(), peopleDTO.getCar(), PeopleService.INSERT);
                if (insertStatus == 1) {
                    log.info("Insert car to t_car successfully");
                } else {
                    log.info("Insert car to t_car failed");
                    return ResponseResult.error(0, "添加人口对象车辆信息表失败");
                }
            }
        }
        log.info("Insert people info successfully");
        return ResponseResult.init(1);
    }

    /**
     * 修改人口对象
     *
     * @param peopleDTO 人口对象信息
     * @return 成功状态【0：修改成功；1：修改失败】
     */
    @ApiOperation(value = "修改人口对象", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.UPDATE_PEOPLE, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.OBJECT_OPERATION + "')")
    public ResponseResult<Integer> updatePeople(@RequestBody @ApiParam(value = "修改人口对象") PeopleDTO peopleDTO) {
        if (peopleDTO == null) {
            log.error("Start Update people info, but people is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口对象信息为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getName())) {
            log.error("Start Update people info, but name is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口对象姓名为空，请检查！");
        }
        if (StringUtils.isBlank(peopleDTO.getIdcard())) {
            log.error("Start Update people info, but idcard is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口对象身份证为空，请检查！");
        }
        if (peopleDTO.getRegion() == null) {
            log.error("Start Update people info, but region is null !");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改人口对象区域为空，请检查！");
        }
        log.info("Start Update people info, DTO :" + JSONUtil.toJson(peopleDTO));
        People people = peopleDTO.peopleDTOShift(peopleDTO);
        log.info("Start Update object info, param is:" + JSONUtil.toJson(people));
        Integer status = peopleService.people(people, PeopleService.UPDATE);
        if (status == null || status != 1) {
            log.info("Update t_people info failed");
            return ResponseResult.error(0, "修改人口对象失败");
        }
        log.info("Update t_people info successfully");
        if (peopleDTO.getFlag() == null && peopleDTO.getIdCardPic() == null && peopleDTO.getCapturePic() == null &&
                peopleDTO.getImsi() == null && peopleDTO.getPhone() == null && peopleDTO.getHouse() == null &&
                peopleDTO.getCar() == null) {
            log.info("Insert people info successfully");
            return ResponseResult.init(status);
        } else {
            if (peopleDTO.getFlag() != null) {
                Integer insertStatus = peopleService.people_flag(people.getId(), peopleDTO.getFlag(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update flag to t_flag successfully");
                } else {
                    log.info("Update flag to t_flag failed");
                    return ResponseResult.error(0, "修改人口对象标签表失败");
                }
            }
            if (peopleDTO.getIdCardPic() != null) {
                Integer insertStatus = peopleService.people_picture(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update idCard pic to t_picture successfully");
                } else {
                    log.info("Update idCard pic to t_picture failed");
                    return ResponseResult.error(0, "修改人口对象证件照片表失败");
                }
            }
            if (peopleDTO.getCapturePic() != null) {
                Integer insertStatus = peopleService.people_picture(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update capture pic to t_picture successfully");
                } else {
                    log.info("Update capture pic to t_picture failed");
                    return ResponseResult.error(0, "修改人口对象实采照片表失败");
                }
            }
            if (peopleDTO.getImsi() != null) {
                Integer insertStatus = peopleService.people_imsi(people.getId(), peopleDTO.getImsi(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update imsi to t_imsi successfully");
                } else {
                    log.info("Update imsi to t_imsi failed");
                    return ResponseResult.error(0, "修改人口对象imsi表失败");
                }
            }
            if (peopleDTO.getPhone() != null) {
                Integer insertStatus = peopleService.people_phone(people.getId(), peopleDTO.getPhone(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update phone to t_phone successfully");
                } else {
                    log.info("Update phone to t_phone failed");
                    return ResponseResult.error(0, "修改人口对象联系方式表失败");
                }
            }
            if (peopleDTO.getHouse() != null) {
                Integer insertStatus = peopleService.people_house(people.getId(), peopleDTO.getHouse(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update house to t_house successfully");
                } else {
                    log.info("Update house to t_house failed");
                    return ResponseResult.error(0, "修改人口对象房产信息表失败");
                }
            }
            if (peopleDTO.getCar() != null) {
                Integer insertStatus = peopleService.people_car(people.getId(), peopleDTO.getCar(), PeopleService.UPDATE);
                if (insertStatus == 1) {
                    log.info("Update car to t_car successfully");
                } else {
                    log.info("Update car to t_car failed");
                    return ResponseResult.error(0, "修改人口对象车辆信息表失败");
                }
            }
        }
        log.info("Update people info successfully");
        return ResponseResult.init(1);
    }

    /**
     * 查询对象
     *
     * @param param 查询条件参数封装
     * @return peopleVO 查询返回参数封装
     */
    @ApiOperation(value = "对象查询", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.OBJECTINFO_SEARCH, method = RequestMethod.POST)
    @PreAuthorize("hasAythority('" + BigDataPermission.OBJECT_VIEW + "')")

    public ResponseResult<List<PeopleVO>> searchPeople(@RequestBody @ApiParam(value = "查询条件") SearchParam param) {
        if (param == null) {
            log.error("Start select people, but param is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查!");
        }
        if (param.getRegionId() == null || param.getRegionId() == 0) {
            log.error("Start select people, but regionID is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,区域ID不能为空,请检查!");
        }
        if (param.getSearchType() != 0 && param.getSearchType() != 1 && param.getSearchType() != 2 && param.getSearchType() != 3) {
            log.error("Start select people, but SearchType is error ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,区域ID不能为空,请检查!");
        }
        log.info("Start select people, search param: " + JSONUtil.toJson(param));
        FilterField field = FilterField.SearchParamShift(param);
        log.info("Start select people, FilterField param: " + JSONUtil.toJson(param));
        List<PeopleVO> peoples = peopleService.searchPeople(field);
        log.info("Start select people successfully ,result " + JSONUtil.toJson(peoples));
        return ResponseResult.init(peoples);
    }
}
