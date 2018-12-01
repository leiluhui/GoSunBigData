package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Imei;

public interface ImeiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Imei imei);

    int insertSelective(Imei record);

    Imei selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Imei imei);

    int updateByPrimaryKey(Imei record);

    Imei selectByPeopleId(String peopleId);

    int delete(String peopleid);
}