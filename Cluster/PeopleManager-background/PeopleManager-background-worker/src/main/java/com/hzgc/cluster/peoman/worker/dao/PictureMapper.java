package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PictureMapper {
    List<Picture> selectPicture(@Param("offset") int offset, @Param("limit") int limit);
    Picture selectByPictureId(Long id);
}