package com.hzgc.cloud.community.dao;

import com.hzgc.cloud.community.model.Count24Hour;
import com.hzgc.cloud.community.param.CaptureDetailsDTO;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface Count24HourMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Count24Hour record);

    int insertSelective(Count24Hour record);

    Count24Hour selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Count24Hour record);

    int updateByPrimaryKey(Count24Hour record);

    List<Count24Hour> countCommunityNewPeopleCapture(CaptureDetailsDTO param);
}