package com.hzgc.cloud.community.dao;

import com.hzgc.cloud.community.model.RecognizeRecord;
import com.hzgc.cloud.community.param.*;

import java.util.List;

public interface RecognizeRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(RecognizeRecord record);

    int insertSelective(RecognizeRecord record);

    RecognizeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RecognizeRecord record);

    int updateByPrimaryKey(RecognizeRecord record);

    List<RecognizeRecord> searchCapture1Month(PeopleCaptureDTO param);

    List<RecognizeRecord> searchPeopleTrack1Month(String peopleid);

    RecognizeRecord searchCommunityOutPeopleLastCapture(String peopleid);

    List<RecognizeRecord> searchCommunityNewPeopleCaptureData(CaptureDetailsDTO param);

    List<ImportantPeopleRecognize> getImportantRecognizeRecord(ImportantRecognizeSearchParam param);

    List<ImportantPeopleRecognizeHistory> getImportantPeopleRecognizeHistory(List<Long> communityIds);

    String getSurlByPeopleId(String peopleid);

    int delete(String peopleid);
}