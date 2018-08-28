package com.hzgc.service.clustering.bean.export;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CapatureLocus implements Serializable{
    private String rowkey;
    private List<Locus> locusList;
}
