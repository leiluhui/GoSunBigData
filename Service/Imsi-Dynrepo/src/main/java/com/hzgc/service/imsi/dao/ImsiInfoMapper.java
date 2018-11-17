package com.hzgc.service.imsi.dao;

import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.service.imsi.model.ImsiParam;
import com.hzgc.service.imsi.model.SearchImsiDTO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface ImsiInfoMapper {

    int insertSelective(ImsiInfo record);

    List <ImsiInfo> selectByTime(ImsiParam record);

    List<ImsiInfo> searchIMSI(SearchImsiDTO searchImsiDTO);
}