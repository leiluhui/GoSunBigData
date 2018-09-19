package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.ConfirmRecord;

public interface ConfirmRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfirmRecord record);

    int insertSelective(ConfirmRecord record);

    ConfirmRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfirmRecord record);

    int updateByPrimaryKey(ConfirmRecord record);

    int countNewPeople(Long community);

    int countOutPeople(Long community);
}