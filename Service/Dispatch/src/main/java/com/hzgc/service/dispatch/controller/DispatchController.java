package com.hzgc.service.dispatch.controller;

import com.hzgc.service.dispatch.service.DispatchService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(value = "/dispatch", tags = "布控库服务")
public class DispatchController {
    @Autowired
    private DispatchService dispatchService;
}
