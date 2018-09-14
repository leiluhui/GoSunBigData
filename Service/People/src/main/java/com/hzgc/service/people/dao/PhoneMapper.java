package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Phone;

import java.util.List;

public interface PhoneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Phone record);

    int insertSelective(Phone record);

    Phone selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Phone record);

    int updateByPrimaryKey(Phone record);

    List<Phone> selectByPeopleId(String peopleid);

    List<Long> selectIdByPeopleId(String peopleid);
}