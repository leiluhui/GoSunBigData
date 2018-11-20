package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.model.NewPeople;
import com.hzgc.service.community.param.AffirmOperationDTO;
import com.hzgc.service.community.param.NewAndOutPeopleCountDTO;
import com.hzgc.service.community.param.NewAndOutPeopleSearchDTO;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface NewPeopleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewPeople record);

    int insertSelective(NewPeople record);

    NewPeople selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewPeople record);

    int updateByPrimaryKey(NewPeople record);

    int countNewPeople(Long communityId);

    List<CountCommunityPeople> countTotalNewPeople(NewAndOutPeopleCountDTO param);

    List<CountCommunityPeople> countConfirmNewPeople(NewAndOutPeopleCountDTO param);

    List<NewPeople> searchCommunityNewPeople(NewAndOutPeopleSearchDTO param);

    Integer updateIsconfirm(AffirmOperationDTO param);

    int delete(String peopleid);
}