package com.hzgc.cloud.peoman.worker.dao;

import com.hzgc.cloud.peoman.worker.model.IMSI;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMSIMapper {
    IMSI selectByIMSI(String imsi);
}