package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.model.ImsiInfo;
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
        ImsiInfo imsiInfo = new ImsiInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Long start = timeParam - Long.valueOf(time) * 1000;
        Long end = timeParam + Long.valueOf(time) * 1000;
        String startTime = sdf.format(start);
        String endTime = sdf.format(end);
        imsiInfo.setStartTime(Long.valueOf(startTime));
        imsiInfo.setEndTime(Long.valueOf(endTime));
        return imsiInfoMapper.selectByTime(imsiInfo);
    }
}
