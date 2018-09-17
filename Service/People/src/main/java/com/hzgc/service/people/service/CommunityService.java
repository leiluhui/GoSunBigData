package com.hzgc.service.people.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.service.people.dao.NewPeopleMapper;
import com.hzgc.service.people.dao.PeopleMapper;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.CommunityPeopleCountVO;
import com.hzgc.service.people.param.CommunityPeopleDTO;
import com.hzgc.service.people.param.CommunityPeopleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommunityService {
    @Autowired
    private PeopleMapper peopleMapper;
    @Autowired
    private NewPeopleMapper newPeopleMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CommunityPeopleCountVO countCommunityPeople(Long communityId) {
        CommunityPeopleCountVO vo = new CommunityPeopleCountVO();
        int communityPeople = peopleMapper.countCommunityPeople(communityId);
        int importantPeople = peopleMapper.countImportantPeople(communityId);
        int carePeople = peopleMapper.countCarePeople(communityId);
        int newPeople = 0;
        int outPeople = 0;
        vo.setCommunityPeoples(communityPeople);
        vo.setImportantPeoples(importantPeople);
        vo.setCarePeoples(carePeople);
        vo.setNewPeoples(newPeople);
        vo.setOutPeoples(outPeople);
        return vo;
    }

    public  List<CommunityPeopleVO> searchCommunityPeople(CommunityPeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCommunityPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<CommunityPeopleVO> searchCommunityImportantPeople(CommunityPeopleDTO param) {
        List<CommunityPeopleVO> voList = new ArrayList();
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchImportantPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<CommunityPeopleVO> searchCommunityCarePeople(CommunityPeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCarePeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    private List<CommunityPeopleVO> shift( List<People> peopleList){
        List<CommunityPeopleVO> voList = new ArrayList<>();
        if (peopleList != null && peopleList.size() > 0) {
            for (People people : peopleList) {
                CommunityPeopleVO vo = new CommunityPeopleVO();
                vo.setId(people.getId());
                vo.setIdCard(people.getIdcard());
                vo.setName(people.getName());
                if (people.getLasttime() != null) {
                    vo.setLastTime(sdf.format(people.getLasttime()));
                }
                voList.add(vo);
            }
        }
        return voList;
    }
}
