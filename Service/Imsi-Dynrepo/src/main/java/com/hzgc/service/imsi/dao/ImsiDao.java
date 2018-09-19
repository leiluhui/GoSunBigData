package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.model.ImsiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ImsiDao {

    @Value(value = "${time}")
    private String time;
    @Autowired
    ImsiInfoMapper imsiInfoMapper;

    public List <ImsiInfo> queryByTime(Long timeParam) {
        ImsiInfo imsiInfo = new ImsiInfo();
        imsiInfo.setStartTime(timeParam - Long.valueOf(time) * 1000);
        imsiInfo.setEndTime(timeParam + Long.valueOf(time) * 1000);
        return imsiInfoMapper.selectByTime(imsiInfo);
    }
}
