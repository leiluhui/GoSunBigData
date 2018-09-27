package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.model.OutPeople;
import com.hzgc.service.community.param.NewAndOutPeopleCountDTO;

import java.util.List;

public interface OutPeopleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OutPeople record);

    int insertSelective(OutPeople record);

    OutPeople selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OutPeople record);

    int updateByPrimaryKey(OutPeople record);

    int countOutPeople(Long communityId);

    List<CountCommunityPeople> countTotalOutPeople(NewAndOutPeopleCountDTO param);

    List<CountCommunityPeople> countConfirmOutPeople(NewAndOutPeopleCountDTO param);
}