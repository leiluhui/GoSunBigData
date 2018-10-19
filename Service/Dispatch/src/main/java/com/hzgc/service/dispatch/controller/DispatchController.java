package com.hzgc.service.dispatch.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.dispatch.param.DispatchDTO;
import com.hzgc.service.dispatch.param.DispatchVO;
import com.hzgc.service.dispatch.param.SearchDispatchDTO;
import com.hzgc.service.dispatch.service.DispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "/dispatch", tags = "布控库服务")
public class DispatchController {
    @Autowired
    private DispatchService dispatchService;

    /**
     * 添加布控对象
     *
     * @param dto 请求参数
     * @return 成功状态 1 ：插入成功 0 ：插入失败
     */
    @ApiOperation(value = "添加布控信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_INSERT_DEPLOY, method = RequestMethod.POST)
    public ResponseResult<Integer> insertDeploy(@RequestBody @ApiParam(name = "入参", value = "布控信息") DispatchDTO dto) {
        if (dto == null) {
            log.error("Start insert people info ,but dto is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加布控信息为空,请检查！");
        }
        if (StringUtils.isBlank(String.valueOf(dto.getRegionId()))) {
            log.error("Start insert people info ,but region is null ");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "布控区域为空,请检查！");
        }
        return null;
    }

    /**
     * 删除布控对象
     *
     * @param id 人员ID
     * @return 成功状态 1 ：删除成功 0 :删除失败
     */
    @ApiOperation(value = "删除布控信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_DELETE_DEPLOY, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deleteDeploy(@ApiParam(name = "人员ID", required = true) @RequestParam String id) {
        return null;
    }

    /**
     * 修改布控信息
     *
     * @param dto 请求参数
     * @return 成功状态 1 ：修改成功 0 :修改失败
     */
    @ApiOperation(value = "修改布控信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_UPDATE_DEPLOY, method = RequestMethod.POST)
    public ResponseResult<Integer> updateDeploy(@RequestBody @ApiParam(name = "入参", value = "人员信息") DispatchDTO dto) {
        return null;
    }

    /**
     * @param id     (布控人员ID)
     * @param status （状态 0：失败， 1：成功）
     */
    @ApiOperation(value = "开启/停止人员布控", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_DEPLOY_STATUS, method = RequestMethod.GET)
    public ResponseResult<Integer> deployStatus(@ApiParam(name = "人员ID", required = true) @RequestParam String id,
                                                @ApiParam(name = "人员状态(0:开启,1:停止)", required = true) @RequestParam int status) {
        return ResponseResult.init(0);
    }

    /**
     * @param id （布控人员ID）
     */
    @ApiOperation(value = "根据人员ID获取布控人照片", response = byte[].class)
    @RequestMapping(value = BigDataPath.DISPATCH_GET_FACE, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFace(@ApiParam(name = "人员ID", required = true) @RequestParam String id) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(null);
    }

    /**
     * @param parm (布控信息封装)
     */
    @ApiOperation(value = "查询人员布控信息（模糊查询）", response = DispatchVO.class)
    @RequestMapping(value = BigDataPath.DISPATCH_SEARCH_DEPLOY, method = RequestMethod.POST)
    public ResponseResult<DispatchVO> searchDeploy(@RequestBody SearchDispatchDTO parm) {
        return ResponseResult.init(new DispatchVO());
    }
}
