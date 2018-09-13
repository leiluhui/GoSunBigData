package com.hzgc.cluster.peoman.dao;

import com.hzgc.cluster.peoman.model.Picture;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureMapper {
    List<Picture> selectFeatureByLimit();
}