package com.hzgc.service.imsi.service;

import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.service.imsi.dao.MacInfoMapper;
import com.hzgc.service.imsi.model.MacInfo;
import com.hzgc.service.imsi.model.MacParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MacService {
    @Autowired
    private MacInfoMapper macInfoMapper;

    public ResponseResult <List <MacInfo>> queryBySns(MacParam macParam) {
        try {
            if (null != macParam.getStartTime() && null != macParam.getEndTime()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String startTime = macParam.getStartTime();
                String endTime = macParam.getEndTime();
                macParam.setStartDateTime(sdf.parse(startTime));
                macParam.setEndDateTime(sdf.parse(endTime));
            }
            List <MacInfo> macInfos = macInfoMapper.selectBySns(macParam);
            return ResponseResult.init(macInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
