package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.CommunityDevice;

public interface CommunityDeviceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommunityDevice record);

    int insertSelective(CommunityDevice record);

    CommunityDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommunityDevice record);

    int updateByPrimaryKey(CommunityDevice record);
}