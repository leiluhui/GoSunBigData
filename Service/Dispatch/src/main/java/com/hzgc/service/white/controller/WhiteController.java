package com.hzgc.service.white.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.white.param.DispatchWhiteDTO;
import com.hzgc.service.white.param.DispatchWhiteVO;
import com.hzgc.service.white.param.SearchDispatchWhiteDTO;
import com.hzgc.service.white.param.SearchDispatchWhiteVO;
import com.hzgc.service.white.service.WhiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "/white", tags = "布控库服务")
public class WhiteController {
    @Autowired
    private WhiteService whiteService;
    /**
     * 添加白名单库对象
     *
     * @param dto 请求参数
     * @return 成功状态 1 ：插入成功 0 ：插入失败
     */
    @ApiOperation(value = "添加白名单信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_INSERT_WHITE, method = RequestMethod.POST)
    public ResponseResult<Integer> insertDispatch_white(@RequestBody @ApiParam(name = "入参", value = "布控信息") DispatchWhiteDTO dto) {
        if (dto == null) {
            log.error("Start insert white info, but dto is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "添加白名单信息为空,请检查 !");
        }
        if (dto.getIpc_list() == null && dto.getIpc_list().size() == 0){
            log.error("Start insert white info, but Ipc_list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "相机选择为空,请检查 ! ");
        }
        if (dto.getName_list() == null && dto.getName_list().size() > 0){
            log.info("Start insert white info,but name is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "姓名为空,请检查 ! ");
        }
        log.info("Start insert white info, dto is :" + JacksonUtil.toJson(dto));
        Integer status = whiteService.insertDispatch_white(dto);
        log.info("Insert info successfully");
        return ResponseResult.init(status);
    }
    /**
     * 删除布控对象
     *
     * @param id ID
     * @return 成功状态 1 ：删除成功 0 :删除失败
     */
    @ApiOperation(value = "删除白名单信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_DELETE_WHITE, method = RequestMethod.DELETE)
    public ResponseResult<Integer> deleteDispatch_white(@ApiParam(value = "ID", required = true) @RequestParam String id) {
        if (id == null || "".equals(id)) {
            log.error("Start delete white info,but id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "删除id为空,请检查");
        }
        log.info("Start delete white info,,id is " + JSONUtils.toJSONString(id));
        Integer status = whiteService.deleteDispatch_white(id);
        if (status == 1) {
            log.info("Delete white info  successfully");
            return ResponseResult.init(1);
        } else {
            log.info("Delete white info failed");
            return ResponseResult.init(0);
        }
    }
    /**
     * 修改信息
     *
     * @param dto 请求参数
     * @return 成功状态 1 ：修改成功 0 :修改失败
     */
    @ApiOperation(value = "修改白名单信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_UPDATE_WHITE, method = RequestMethod.POST)
    public ResponseResult<Integer> update_Dispatch_white(@RequestBody @ApiParam(name = "入参", value = "人员信息") DispatchWhiteDTO dto) {
        if (dto == null) {
            log.error("Start update white info,but dto is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象为空,请检查!");
        }
        if (StringUtils.isBlank(dto.getId())) {
            log.error("Start update white info,but id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "修改对象ID为空,请检查!");
        }
        log.info("Start update white info ,dto : " + JacksonUtil.toJson(dto));
        Integer status = whiteService.update_Dispatch_white(dto);
        if (status == 1) {
            log.info("Update white info sucessfully");
            return ResponseResult.init(1);
        } else {
            log.info("Update white info failed");
            return ResponseResult.init(0);
        }
    }
    /**
     * @param id (人员ID)
     * @parm status （状态 0：开启，1：停止）
     */
    @ApiOperation(value = "开启/停止白名单人员", response = Integer.class)
    @RequestMapping(value = BigDataPath.DISPATCH_WHITE_STATUS, method = RequestMethod.GET)
    public ResponseResult<Integer> dispatch_whiteStatus(@ApiParam(value = "人员ID", required = true) @RequestParam String id,
                                                        @ApiParam(value = "人员状态(0:开启,1:停止)", required = true) @RequestParam int status) {
        if (id == null) {
            log.error("Start update white status, but id is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID为空，请检查");
        }
        if (status != 0 && status != 1) {
            log.error("Start update white status, but status is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员状态错误，请检查");
        }
        log.info("Start update white status, id: " + id + ", status: " + status);
        Integer i = whiteService.dispatch_whiteStatus(id,status);
        if (i == 1) {
            log.info("Update white status successfully");
            return ResponseResult.init(1);
        }
        return ResponseResult.error(0, "修改布控人员状态失败！");
    }

    /**
     *
     */
    @ApiOperation(value = "查询人员白名单信息（模糊查询）", response = DispatchWhiteVO.class)
    @RequestMapping(value = BigDataPath.DISPATCH_SEARCH_WHITE, method = RequestMethod.POST)
    public ResponseResult<SearchDispatchWhiteVO> searchDispatch_white(@RequestBody SearchDispatchWhiteDTO dto) {
        if (dto == null) {
            log.error("Start search white info , but dto is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查");
        }
        if (dto.getType() == null || "".equals(dto.getType())){
            log.error("Start search white info, but type is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询名称为空，请检查");
        }
        if (dto.getStart() < 0) {
            log.error("Start search info, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (dto.getLimit() <= 0) {
            log.error("Start search info, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search dispatch, search dispatch:" + JacksonUtil.toJson(dto));
        SearchDispatchWhiteVO vo = whiteService.searchDispatch_white(dto);
        log.info("Search search dispatch successfully, result:" + JacksonUtil.toJson(vo));
        return ResponseResult.init(vo, vo != null ? vo.getTotal() : 0);
    }
}
