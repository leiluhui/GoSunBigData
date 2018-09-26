package com.hzgc.service.people.dao;

import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.param.NewAndOutPeopleCountDTO;
import com.hzgc.service.people.model.People;
import com.hzgc.service.people.param.FilterField;

import java.util.List;

public interface PeopleMapper {
    int deleteByPrimaryKey(String id);

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(People record);

    int updateByPrimaryKey(People record);

    List<People> searchPeople(FilterField field);

    List<Long> searchCommunityIdsByRegionId(Long region);

    int countCommunityPeople(Long community);

    int countImportantPeople(Long community);

    int countCarePeople(Long community);

    List<People> searchCommunityPeople(Long community);

    List<People> searchImportantPeople(Long community);

    List<People> searchCarePeople(Long community);

    List<People> searchNewPeople(Long community);

    List<People> searchOutPeople(Long community);

    List<CountCommunityPeople> countSuggestOutPeople(NewAndOutPeopleCountDTO param);
}