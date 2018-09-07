package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Phone;

public interface PhoneMapper {

    int insert(Phone record);

    int insertSelective(Phone record);

    Phone selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Phone record);

    int updateByPrimaryKey(Phone record);
}