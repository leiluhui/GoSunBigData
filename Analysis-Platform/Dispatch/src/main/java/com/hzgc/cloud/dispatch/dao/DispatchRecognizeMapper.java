package com.hzgc.cloud.dispatch.dao;

import com.hzgc.cloud.dispatch.model.DispatchRecognize;
import com.hzgc.cloud.dispatch.param.DispatchRecognizeDTO;
import com.hzgc.cloud.dispatch.param.DispatchRecognizeVO;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface DispatchRecognizeMapper {
    int deleteByPrimaryKey(String id);

    int insert(DispatchRecognize record);

    int insertSelective(DispatchRecognize record);

    List<DispatchRecognize> selectSelective(DispatchRecognizeDTO record);

    DispatchRecognize selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DispatchRecognize record);

    List<DispatchRecognizeVO> selectDispatchRecognize(DispatchRecognizeDTO dispatchRecognizeDTO);

    int updateByPrimaryKey(DispatchRecognize record);
}