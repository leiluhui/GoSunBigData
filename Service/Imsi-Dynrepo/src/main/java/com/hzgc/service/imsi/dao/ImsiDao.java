package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.model.ImsiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImsiDao {

    @Value(value = "${query.time}")
    private String time;
    @Autowired
    ImsiInfoMapper imsiInfoMapper;

    public List <ImsiParam> queryByTime(Long timeParam) {
        ImsiParam imsiInfo = new ImsiParam();
        imsiInfo.setStartTime(timeParam - Long.valueOf(time) * 1000);
        imsiInfo.setEndTime(timeParam + Long.valueOf(time) * 1000);
        return imsiInfoMapper.selectByTime(imsiInfo);
    }
}
