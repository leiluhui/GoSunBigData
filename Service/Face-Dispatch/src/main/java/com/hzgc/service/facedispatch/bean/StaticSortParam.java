package com.hzgc.service.facedispatch.bean;

import java.io.Serializable;

/**
 * 以图搜图:排序参数
 */
public enum  StaticSortParam implements Serializable {
    PEKEY,          // 人员类型
    TIMEASC,        // 时间升序
    TIMEDESC,       // 时间降序
    RELATEDASC,     // 相似度升序
    RELATEDDESC,    // 相似度降序
}

