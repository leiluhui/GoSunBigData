package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.RecognizeRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecognizeRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(RecognizeRecord record);

    int insertUpdate(RecognizeRecord record);

    int insertSelective(RecognizeRecord record);

    RecognizeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecognizeRecord record);

    int updateByPrimaryKey(RecognizeRecord record);
}