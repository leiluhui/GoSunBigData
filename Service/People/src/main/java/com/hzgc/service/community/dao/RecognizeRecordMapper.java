package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.RecognizeRecord;

public interface RecognizeRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(RecognizeRecord record);

    int insertSelective(RecognizeRecord record);

    RecognizeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecognizeRecord record);

    int updateByPrimaryKey(RecognizeRecord record);
}