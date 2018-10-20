package com.hzgc.service.dispatch.dao;

import com.hzgc.service.dispatch.model.Dispatch;
import com.hzgc.service.dispatch.param.DispatchDTO;
import com.hzgc.service.dispatch.param.SearchDispatchDTO;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface DispatchMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dispatch record);

    int insertSelective(Dispatch record);

    Dispatch selectByPrimaryKey(String id);

    Dispatch selectFaceById(String id);

    int updateByPrimaryKeySelective(Dispatch record);

    int updateStatusById(Dispatch record);

    int updateByPrimaryKeyWithBLOBs(Dispatch record);

    int updateByPrimaryKey(Dispatch record);

    List<Dispatch> searchDispatch(SearchDispatchDTO searchDispatchDTO);
}