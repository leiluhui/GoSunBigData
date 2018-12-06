package com.hzgc.cluster.peoman.worker.dao;

import com.hzgc.cluster.peoman.worker.model.InnerFeature;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InnerFeatureMapper {
    List<InnerFeature> select();
    List<InnerFeature> selectByPeopleId(String peopleId);
    int insert(InnerFeature innerFeature);
    int delete(String peopleid);
    int deleteAll();
}