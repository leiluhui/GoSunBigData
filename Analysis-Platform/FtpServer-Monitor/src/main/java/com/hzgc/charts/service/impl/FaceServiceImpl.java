package com.hzgc.charts.service.impl;

import com.hzgc.charts.dao.FaceRepository;
import com.hzgc.charts.domain.Face;
import com.hzgc.charts.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liang on 2018/12/12
 */
@Service
public class FaceServiceImpl implements FaceService {

    @Autowired
    private FaceRepository faceRepository;

    @Override
    public List<Face> findAll() {

        Iterable<Face> userIterable = faceRepository.findAll();
        List<Face> list = new ArrayList<>();
        userIterable.forEach(single ->list.add(single));

        return list;
    }

    @Override
    public long findTotalNum() {
        return faceRepository.count();
    }

}
