package com.hzgc.cluster.dispatch.dao;

import com.hzgc.cluster.dispatch.model.DispatchIpc;
import org.springframework.stereotype.Component;

import java.util.List;

public interface DispatchIpcMapper {
    int insert(DispatchIpc record);

    int insertSelective(DispatchIpc record);

    List<DispatchIpc> selectById(String defid);
}