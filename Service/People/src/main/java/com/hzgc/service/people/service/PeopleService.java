package com.hzgc.service.people.service;

import com.hzgc.service.people.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PeopleService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private FlagMapper flagMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private ImsiMapper imsiMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private PeopleMapper peopleMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    @Autowired
    private PictureMapper pictureMapper;


}
