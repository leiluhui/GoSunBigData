package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.Flag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlagMapper {
    Flag selectByPrimaryKey(String peopleid);
}