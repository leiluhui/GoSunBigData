package com.hzgc.service.dispatch.dao;

import com.hzgc.service.dispatch.model.Dispatch;
import com.hzgc.service.dispatch.param.DispatchDTO;
import com.hzgc.service.dispatch.param.DispatchVO;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface DispatchMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dispatch record);

    int insertSelective(Dispatch record);

    Dispatch selectByPrimaryKey(String id);

    Dispatch selectSelective(DispatchDTO record);

    int updateByPrimaryKeySelective(Dispatch record);

    int updateByPrimaryKeyWithBLOBs(Dispatch record);

    int updateByPrimaryKey(Dispatch record);
}