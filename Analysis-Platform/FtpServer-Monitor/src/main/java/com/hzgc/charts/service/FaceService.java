package com.hzgc.charts.service;

import com.hzgc.charts.domain.Face;

import java.util.List;

/**
 * created by liang on 2018/12/12
 */
public interface FaceService {


     /**
      * 查询所有的car的数量
      */
     long findTotalNum();


     List<Face> findAll();
}
