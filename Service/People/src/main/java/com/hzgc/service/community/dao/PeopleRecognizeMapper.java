package com.hzgc.service.community.dao;

import com.hzgc.service.community.model.PeopleRecognize;

import java.util.List;

public interface PeopleRecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PeopleRecognize record);

    int insertSelective(PeopleRecognize record);

    PeopleRecognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PeopleRecognize record);

    int updateByPrimaryKey(PeopleRecognize record);

    List<PeopleRecognize> searchCapture1Month(String peopleid);

    PeopleRecognize searchCommunityOutPeopleLastCapture(String peopleid);

    List<PeopleRecognize> searchCommunityNewPeopleCaptureData(String peopleid);

    String getSurlByPeopleId(String peopleid);
}