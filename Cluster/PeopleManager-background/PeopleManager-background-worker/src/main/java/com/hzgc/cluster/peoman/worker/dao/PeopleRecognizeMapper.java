package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.PeopleRecognize;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PeopleRecognizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PeopleRecognize record);

    int insertSelective(PeopleRecognize record);

    PeopleRecognize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PeopleRecognize record);

    int updateByPrimaryKey(PeopleRecognize record);
}