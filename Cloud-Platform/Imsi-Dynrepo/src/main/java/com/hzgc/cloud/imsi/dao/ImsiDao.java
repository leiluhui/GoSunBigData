package com.hzgc.cloud.imsi.dao;

import com.hzgc.cloud.imsi.model.ImsiParam;
import com.hzgc.common.service.imsi.ImsiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class ImsiDao {

    @Value(value = "${query.time}")
    private String time;
    @Autowired
    ImsiInfoMapper imsiInfoMapper;

    public List <ImsiInfo> queryByTime(Long timeParam) {
        ImsiParam imsiParam = new ImsiParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long startTime = (timeParam - Long.valueOf(time) * 1000);
        Long endTime = (timeParam + Long.valueOf(time) * 1000);
        imsiParam.setStartTime(sdf.format(startTime));
        imsiParam.setEndTime(sdf.format(endTime));
        return imsiInfoMapper.selectByTime(imsiParam);
    }
}
