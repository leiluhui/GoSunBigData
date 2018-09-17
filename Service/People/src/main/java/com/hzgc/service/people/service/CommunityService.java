package com.hzgc.service.people.service;

import com.hzgc.service.people.dao.NewPeopleMapper;
import com.hzgc.service.people.dao.PeopleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommunityService {
    @Autowired
    private PeopleMapper peopleMapper;
    @Autowired
    private NewPeopleMapper newPeopleMapper;
}
