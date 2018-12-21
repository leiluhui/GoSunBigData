package com.hzgc.cloud.community.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.cloud.community.dao.NewPeopleMapper;
import com.hzgc.cloud.community.dao.OutPeopleMapper;
import com.hzgc.cloud.community.model.Count;
import com.hzgc.cloud.community.param.PeopleCountVO;
import com.hzgc.cloud.community.param.PeopleDTO;
import com.hzgc.cloud.community.param.PeopleVO;
import com.hzgc.cloud.people.dao.PeopleMapper;
import com.hzgc.cloud.people.model.People;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PeopleCountService {
    @Autowired
    @SuppressWarnings("unused")
    private PeopleMapper peopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private NewPeopleMapper newPeopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private OutPeopleMapper outPeopleMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PeopleCountVO countCommunityPeople(Long communityId) {
        PeopleCountVO vo = new PeopleCountVO();
        vo.setCommunityPeoples(peopleMapper.countCommunityPeople(communityId));
        // 重点人员统计条件：1.当前小区；2.符合重点标签；3.此人员须有照片
        vo.setImportantPeoples(peopleMapper.countImportantPeople(communityId));
        // 关爱人员统计条件：1.当前小区；2.符合关爱标签；3.此人员须有照片
        vo.setCarePeoples(peopleMapper.countCarePeople(communityId));
        vo.setNewPeoples(newPeopleMapper.countNewPeople(communityId));
        vo.setOutPeoples(outPeopleMapper.countOutPeople(communityId));
        return vo;
    }

    public int countGridPeople(Long gridCode) {
        return peopleMapper.countGridPeople(gridCode);
    }

    public List<Count> countGridFlagPeople(Long gridCode) {
        // 人员统计条件：1.当前网格；2.符合当前标签；3.此人员须有照片
        return peopleMapper.countFlagPeople(gridCode);
    }

    public List<PeopleVO> searchCommunityPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCommunityPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    // 重点人员查询条件：1.当前小区；2.符合重点标签；3.此人员须有照片
    public List<PeopleVO> searchCommunityImportantPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchImportantPeople(param);
        return this.shift(peopleList);
    }

    // 关爱人员查询条件：1.当前小区；2.符合关爱标签；3.此人员须有照片
    public List<PeopleVO> searchCommunityCarePeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCarePeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityNewPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchNewPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityOutPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchOutPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    private List<PeopleVO> shift(List<People> peopleList) {
        List<PeopleVO> voList = new ArrayList<>();
        if (peopleList != null && peopleList.size() > 0) {
            for (People people : peopleList) {
                PeopleVO vo = new PeopleVO();
                vo.setId(people.getId());
                vo.setIdCard(people.getIdcard());
                vo.setName(people.getName());
                if (people.getLasttime() != null) {
                    vo.setLastTime(sdf.format(people.getLasttime()));
                }
                vo.setPictureId(people.getPictureId());
                voList.add(vo);
            }
        }
        return voList;
    }
}
