package com.hzgc.cluster.peoman.worker.service;

import lombok.Data;

@Data
public class CompareRes {
    //集合下标
    private Integer index;
    //识别标签(2 : 新增, 10 ： 完全新增(原图))
    private Integer flag;
    //完全新增比对相似度
    private Float similarity;
}
