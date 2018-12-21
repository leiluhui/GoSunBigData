package com.hzgc.cloud.people.controller;

import com.hzgc.cloud.people.param.ImeiVO;
import com.hzgc.cloud.people.param.MentalPatientDTO;
import com.hzgc.cloud.people.param.PeopleVO;
import com.hzgc.cloud.people.service.MentalPatientService;
import com.hzgc.cloud.people.service.PeopleService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
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

@RestController
@Api(value = "/people", tags = "精神病人口库服务")
@Slf4j
public class MentalPatientController {
    @Autowired
    @SuppressWarnings("unused")
   private MentalPatientService  mentalPatientService;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleService peopleService;

    @ApiOperation(value = "添加精神病人手环信息", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.MENTALPATIENT_INSERT, method = RequestMethod.POST)
    public ResponseResult<Integer> insertMentalPatient(@RequestBody @ApiParam(name = "入参", value = "精神病人手环信息") MentalPatientDTO pram) {
        if (pram == null) {
            log.error("Start insert mentalPatient info, but pram is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加信息为空，请检查！");
        }
        if (StringUtils.isBlank(pram.getGuardianName())) {
            log.error("Start insert mentalPatient info, but guardianName is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加监护人姓名为空，请检查！");
        }
        if (StringUtils.isBlank(pram.getImei())) {
            log.error("Start insert mentalPatient info, but Imei is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加Imei为空，请检查！");
        }
        log.info("Start insert mentalPatient info, param DTO:" + JacksonUtil.toJson(pram));
        int status = mentalPatientService.insertMentalPatient(pram);
        if (status != 1){
            log.error("Insert mentalPatient info failed");
            return ResponseResult.init(0);
        }
        log.info("Insert mentalPatient info successfully");
        return ResponseResult.init(status);
    }

    @ApiOperation(value = "修改精神病人手环信息", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.MENTALPATIENT_UPDATE, method = RequestMethod.PUT)
    public ResponseResult<Integer> updateMentalPatient(@RequestBody @ApiParam(name = "入参", value = "精神病人手环信息") MentalPatientDTO param) {
        if (param == null) {
            log.error("Start update mentalPatient info, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改信息为空，请检查！");
        }
        if (param.getId() == null) {
            log.error("Start update mentalPatient info, but imei id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改ID为空，请检查！");
        }
        log.info("Start update mentalPatient info, param DTO:" + JacksonUtil.toJson(param));
        int status = mentalPatientService.updateMentalPatient(param);
        if (status != 1){
            log.error("Update mentalPatient info failed");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改精神病人手环信息失败");
        }
        return ResponseResult.init(1);
    }

    @ApiOperation(value = "根据ID查询精神病人手环信息", response = ImeiVO.class)
    @RequestMapping(value = BigDataPath.MENTALPATIENT_SELECT_BY_PEOPLEID, method = RequestMethod.GET)
    public ResponseResult<ImeiVO> selectMentalPatientByPeopleId(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start select mentalPatient info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询ID为空，请检查！");
        }
        log.info("Start select mentalPatient info, people id is:" + peopleId);
        ImeiVO vo = mentalPatientService.selectMentalPatientByPeopleId(peopleId);
        log.info("Select mentalPatient info successfully, result:" + JacksonUtil.toJson(vo));
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "根据精神病手环ID(IMEI)查询人口信息", response = PeopleVO.class)
    @RequestMapping(value = BigDataPath.PEOPLE_SELECT_BY_IMEIID, method = RequestMethod.GET)
    public ResponseResult<PeopleVO> selectByImeiId(String imeiId) {
        if (StringUtils.isBlank(imeiId)) {
            log.error("Start select people info, but IMEI ID is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询手环ID为空，请检查！");
        }
        log.info("Start select people info, IMEI ID is:" + imeiId);
        String peopleId = mentalPatientService.selectPeopleIdByImeiId(imeiId);
        if (StringUtils.isBlank(peopleId)){
            log.error("Start select people info, but get people ID is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "手环信息未在人口库中绑定，请检查！");
        }
        log.info("Start select people info, people ID is:" + peopleId);
        PeopleVO peopleVO = peopleService.selectByPeopleId(peopleId);
        log.info("Select people info successfully, result:" + JacksonUtil.toJson(peopleVO));
        return ResponseResult.init(peopleVO);
    }
}
