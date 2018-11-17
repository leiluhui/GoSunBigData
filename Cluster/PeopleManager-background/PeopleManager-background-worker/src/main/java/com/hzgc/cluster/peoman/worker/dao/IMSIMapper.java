package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.IMSI;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMSIMapper {
    IMSI selectByIMSI(String imsi);
}