package com.hzgc.service.people.controller;

import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.people.service.ParamPickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api(value = "/people", tags = "人口库服务")
public class ParamPickController {
    @Autowired
    private ParamPickService paramPickService;

    /**
     * 文化程度选项
     *
     * @return map
     */
    @ApiOperation(value = "文化程度选项", response = Map.class)
    @RequestMapping(value = BigDataPath.PEOPLE_EDULEVEL, method = RequestMethod.GET)
    public ResponseResult<Map<Integer, String>> getEdulevelPick() {
        Map<Integer, String> map =  paramPickService.getEdulevelPick();
        return ResponseResult.init(map);
    }

    /**
     * 标签选项
     *
     * @return map
     */
    @ApiOperation(value = "标签选项", response = Map.class)
    @RequestMapping(value = BigDataPath.PEOPLE_FLAG, method = RequestMethod.GET)
    public ResponseResult<Map<Integer, String>> getFlagPick() {
        Map<Integer, String> map =  paramPickService.getFlagPick();
        return ResponseResult.init(map);
    }

    /**
     * 政治面貌选项
     *
     * @return map
     */
    @ApiOperation(value = "政治面貌选项", response = Map.class)
    @RequestMapping(value = BigDataPath.PEOPLE_POLITIC, method = RequestMethod.GET)
    public ResponseResult<Map<Integer, String>> getPoliticPick() {
        Map<Integer, String> map =  paramPickService.getPoliticPick();
        return ResponseResult.init(map);
    }

    /**
     * 全国省级目录选项
     *
     * @return map
     */
    @ApiOperation(value = "全国省级目录选项", response = Map.class)
    @RequestMapping(value = BigDataPath.PEOPLE_PROVINCES, method = RequestMethod.GET)
    public ResponseResult<Map<Integer, String>> getProvincesPick() {
        Map<Integer, String> map =  paramPickService.getProvincesPick();
        return ResponseResult.init(map);
    }

    /**
     * 某省级下所有市级目录选项
     *
     * @return map
     */
    @ApiOperation(value = "市级目录选项", response = Map.class)
    @RequestMapping(value = BigDataPath.PEOPLE_CITY, method = RequestMethod.GET)
    public ResponseResult<Map<Integer, String>> getCityPick(int index) {
        Map<Integer, String> map =  paramPickService.getCityPick(index);
        return ResponseResult.init(map);
    }
}
