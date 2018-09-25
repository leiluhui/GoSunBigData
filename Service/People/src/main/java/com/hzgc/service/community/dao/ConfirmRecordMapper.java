package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.ConfirmRecord;
import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.param.SuggestPeopleDTO;

import java.util.List;

public interface ConfirmRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfirmRecord record);

    int insertSelective(ConfirmRecord record);

    ConfirmRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfirmRecord record);

    int updateByPrimaryKey(ConfirmRecord record);

    int countNewPeople(Long community);

    int countOutPeople(Long community);

    List<CountCommunityPeople> countConfirmNewPeople(SuggestPeopleDTO param);

    List<CountCommunityPeople> countConfirmOutPeople(SuggestPeopleDTO param);
}