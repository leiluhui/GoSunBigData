package com.hzgc.service.people.controller;

import com.hzgc.service.people.service.PeopleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/people", tags = "人口库服务")
@Slf4j
public class PeopleController {
    @Autowired
    @SuppressWarnings("unused")
    private PeopleService peopleService;
}
