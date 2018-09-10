package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Phone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhoneMapper {

    int insert(Phone record);

    int insertSelective(Phone record);

    Phone selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Phone record);

    int updateByPrimaryKey(Phone record);

    List<Phone> selectPhoneIdsByPhone(String phone);
}