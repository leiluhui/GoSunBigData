package com.hzgc.service.people.dao;

import com.hzgc.service.people.model.Picture;
import com.hzgc.service.people.model.PictureWithBLOBs;

public interface PictureMapper {

    int insert(PictureWithBLOBs record);

    int insertSelective(PictureWithBLOBs record);

    PictureWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PictureWithBLOBs record);

    int updateByPrimaryKey(Picture record);
}