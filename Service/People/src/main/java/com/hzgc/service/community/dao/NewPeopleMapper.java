package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.model.NewPeople;
import com.hzgc.service.community.param.SuggestPeopleDTO;

import java.util.List;

public interface NewPeopleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewPeople record);

    int insertSelective(NewPeople record);

    NewPeople selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewPeople record);

    int updateByPrimaryKey(NewPeople record);

    List<CountCommunityPeople> countSuggestNewPeople(SuggestPeopleDTO param);
}