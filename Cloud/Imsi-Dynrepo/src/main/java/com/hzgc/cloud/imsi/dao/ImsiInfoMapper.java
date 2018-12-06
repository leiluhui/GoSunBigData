package com.hzgc.cloud.imsi.dao;

import com.hzgc.cloud.imsi.model.ImsiParam;
import com.hzgc.cloud.imsi.model.SearchImsiParam;
import com.hzgc.common.service.imsi.ImsiInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface ImsiInfoMapper {

    int insertSelective(ImsiInfo record);

    List <ImsiInfo> selectByTime(ImsiParam record);

    List<ImsiInfo> searchIMSI(SearchImsiParam param);
}