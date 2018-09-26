package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.ConfirmRecord;
import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.param.NewAndOutPeopleCountDTO;

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

    List<CountCommunityPeople> countConfirmNewPeople(NewAndOutPeopleCountDTO param);

    List<CountCommunityPeople> countConfirmOutPeople(NewAndOutPeopleCountDTO param);
}