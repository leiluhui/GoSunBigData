package com.hzgc.service.imsi.service;

import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.service.imsi.dao.ImsiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImsiService {

    @Autowired
    private ImsiDao imsiDao;

    public List <ImsiInfo> queryByTime(Long time) {
        return imsiDao.queryByTime(time);
    }
}
