package com.hzgc.cluster.peoman.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.cluster.peoman.dao.PictureMapper;
import com.hzgc.cluster.peoman.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PictureService {
    @Autowired
    @SuppressWarnings("unused")
    private PictureMapper pictureMapper;

    public List<Picture> getData(String param) {
        int offset = Integer.parseInt(param.substring(0,param.indexOf("_") - 1));
        int limit = Integer.parseInt(param.substring(param.indexOf("_") +1)) - offset + 1;
        PageHelper.offsetPage(offset, limit);
        List<Picture> pictureList = pictureMapper.selectFeatureByLimit();
        return pictureList;
    }
}
