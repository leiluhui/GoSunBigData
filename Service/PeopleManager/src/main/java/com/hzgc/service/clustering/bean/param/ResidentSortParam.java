package com.hzgc.service.clustering.bean.param;

import java.io.Serializable;

public enum ResidentSortParam implements Serializable{
    TIMEASC,        // 时间升序
    TIMEDESC,       // 时间降序
    RELATEDASC,     // 相似度升序
    RELATEDDESC,    // 相似度降序
    IMPORTANTASC,   // 重点人员升序
    IMPORTANTDESC   // 重点人员降序
}
