package com.hzgc.service.people.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.people.service.ParamPickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Api(value = "/people", tags = "人口库服务")
public class ParamPickController {
    @Autowired
    private ParamPickService paramPickService;

    @ApiOperation(value = "文化程度选项", response = ParamPickService.Param.class)
    @RequestMapping(value = BigDataPath.PEOPLE_EDULEVEL, method = RequestMethod.GET)
    public ResponseResult<List<ParamPickService.Param>> getEdulevelPick() {
        List<ParamPickService.Param> list = paramPickService.getEdulevelPick();
        return ResponseResult.init(list);
    }

    @ApiOperation(value = "标签选项", response = ParamPickService.Param.class)
    @RequestMapping(value = BigDataPath.PEOPLE_FLAG, method = RequestMethod.GET)
    public ResponseResult<List<ParamPickService.Param>> getFlagPick() {
        List<ParamPickService.Param> list = paramPickService.getFlagPick();
        return ResponseResult.init(list);
    }

    @ApiOperation(value = "政治面貌选项", response = ParamPickService.Param.class)
    @RequestMapping(value = BigDataPath.PEOPLE_POLITIC, method = RequestMethod.GET)
    public ResponseResult<List<ParamPickService.Param>> getPoliticPick() {
        List<ParamPickService.Param> list = paramPickService.getPoliticPick();
        return ResponseResult.init(list);
    }

    @ApiOperation(value = "全国省级目录选项", response = ParamPickService.Param.class)
    @RequestMapping(value = BigDataPath.PEOPLE_PROVINCES, method = RequestMethod.GET)
    public ResponseResult<List<ParamPickService.Param>> getProvincesPick() {
        List<ParamPickService.Param> list = paramPickService.getProvincesPick();
        return ResponseResult.init(list);
    }


    @ApiOperation(value = "市级目录选项", response = ParamPickService.Param.class)
    @RequestMapping(value = BigDataPath.PEOPLE_CITY, method = RequestMethod.GET)
    public ResponseResult<List<ParamPickService.Param>> getCityPick(Integer index) {
        if (index == null) {
            log.error("Start city pick, but index is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空,请检查！");
        }
        List<ParamPickService.Param> list = paramPickService.getCityPick(index);
        return ResponseResult.init(list);
    }
}
