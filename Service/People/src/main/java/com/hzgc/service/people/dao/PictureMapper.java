package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Picture;
import com.hzgc.service.people.model.PictureWithBLOBs;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PictureWithBLOBs record);

    int insertSelective(PictureWithBLOBs record);

    PictureWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PictureWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PictureWithBLOBs record);

    int updateByPrimaryKey(Picture record);

    List<PictureWithBLOBs> selectByPeopleId(String peopleid);

    PictureWithBLOBs selectPictureById(Long id);

    List<PictureWithBLOBs> selectPictureByPeopleId(String peopleid);

    int deleteByPeopleId(String peopleId);
}