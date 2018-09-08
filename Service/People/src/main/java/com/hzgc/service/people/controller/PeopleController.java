package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.service.rest.BigDataPermission;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.PeopleDTO;
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
    @RequestMapping(value = BigDataPath.OBJECTINFO_ADD, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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
        Long peopleId = peopleService.insertPeople(people);
        if (peopleId == null) {
            log.info("Insert t_people info failed");
            return ResponseResult.error(0, "添加人口对象失败");
        }
        log.info("Insert t_people info successfully");
        if (people.getFlag() == null && people.getIdcardpic() == null && people.getCapturepic() == null &&
                people.getImsi() == null && people.getPhone() == null && people.getHouse() == null &&
                people.getCar() == null) {
            log.info("Insert people info successfully");
            return ResponseResult.init(Integer.valueOf(String.valueOf(peopleId)));
        } else {
            if (people.getFlag() != null) {
                Integer insertStatus = peopleService.insertPeople_flag(people.getFlag(), peopleDTO.getFlag());
                if (insertStatus == 1) {
                    log.info("Insert flag to t_flag successfully");
                } else {
                    log.info("Insert flag to t_flag failed");
                    return ResponseResult.error(0, "添加人口对象标签表失败");
                }
            }
            if (people.getIdcardpic() != null) {
                Integer insertStatus = peopleService.insertPeople_idcardpic(peopleId, people.getIdcardpic(), peopleDTO.getIdCardPic());
                if (insertStatus == 1) {
                    log.info("Insert idCard pic to t_picture successfully");
                } else {
                    log.info("Insert idCard pic to t_picture failed");
                    return ResponseResult.error(0, "添加人口对象证件照片表失败");
                }
            }
            if (people.getCapturepic() != null) {
                Integer insertStatus = peopleService.insertPeople_capturepic(peopleId, people.getCapturepic(), peopleDTO.getCapturePic());
                if (insertStatus == 1) {
                    log.info("Insert capture pic to t_picture successfully");
                } else {
                    log.info("Insert capture pic to t_picture failed");
                    return ResponseResult.error(0, "添加人口对象实采照片表失败");
                }
            }
            if (people.getImsi() != null) {
                Integer insertStatus = peopleService.insertPeople_imsi(people.getImsi(), peopleDTO.getImsi());
                if (insertStatus == 1) {
                    log.info("Insert imsi to t_imsi successfully");
                } else {
                    log.info("Insert imsi to t_imsi failed");
                    return ResponseResult.error(0, "添加人口对象imsi表失败");
                }
            }
            if (people.getPhone() != null) {
                Integer insertStatus = peopleService.insertPeople_phone(people.getPhone(), peopleDTO.getPhone());
                if (insertStatus == 1) {
                    log.info("Insert phone to t_phone successfully");
                } else {
                    log.info("Insert phone to t_phone failed");
                    return ResponseResult.error(0, "添加人口对象联系方式表失败");
                }
            }
            if (people.getHouse() != null) {
                Integer insertStatus = peopleService.insertPeople_house(people.getHouse(), peopleDTO.getHouse());
                if (insertStatus == 1) {
                    log.info("Insert house to t_house successfully");
                } else {
                    log.info("Insert house to t_house failed");
                    return ResponseResult.error(0, "添加人口对象房产信息表失败");
                }
            }
            if (people.getCar() != null) {
                Integer insertStatus = peopleService.insertPeople_car(people.getCar(), peopleDTO.getCar());
                if (insertStatus == 1) {
                    log.info("Insert car to t_car successfully");
                } else {
                    log.info("Insert car to t_car failed");
                    return ResponseResult.error(0, "添加人口对象车辆信息表失败");
                }
            }
        }
        log.info("Insert people info successfully");
        return ResponseResult.init(Integer.valueOf(String.valueOf(peopleId)));
    }
}
